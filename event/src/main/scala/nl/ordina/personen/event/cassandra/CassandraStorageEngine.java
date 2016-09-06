package nl.ordina.personen.event.cassandra;

import com.datastax.driver.core.*;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import org.axonframework.common.transaction.TransactionManager;
import org.axonframework.eventhandling.EventMessage;
import org.axonframework.eventsourcing.DomainEventMessage;
import org.axonframework.eventsourcing.eventstore.*;
import org.axonframework.eventsourcing.eventstore.jdbc.EventSchema;
import org.axonframework.eventsourcing.eventstore.legacy.LegacyTrackingToken;
import org.axonframework.messaging.unitofwork.CurrentUnitOfWork;
import org.axonframework.messaging.unitofwork.UnitOfWork;
import org.axonframework.serialization.Serializer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Created by gle21221 on 2-9-2016.
 */
public class CassandraStorageEngine extends BatchingEventStorageEngine {

    private static final String INDEX = "BRP";
    private final Session session;
    private final EventSchema schema;
    private final Mapper<DomainEventEntry> eventMapper;
    private final Mapper<SnapshotEventEntry> snapshotMapper;
    private final Mapper<BatchEntry> batchMapper;
    private final TupleType batchEventType;

    public CassandraStorageEngine(TransactionManager transactionManager, Session session) {
        this(transactionManager, session, new EventSchema());
    }

    public CassandraStorageEngine(TransactionManager transactionManager, Session session, EventSchema schema) {
        super(transactionManager);
        this.session = session;
        this.schema = schema;
        MappingManager mappingManager = new MappingManager(session);
        this.eventMapper = mappingManager.mapper(DomainEventEntry.class);
        this.snapshotMapper = mappingManager.mapper(SnapshotEventEntry.class);
        this.batchMapper = mappingManager.mapper(BatchEntry.class);
        this.batchEventType = session.getCluster().getMetadata().newTupleType(TupleType.varchar(), TupleType.bigint());
    }

    @Override
    protected List<DomainEventEntry> fetchTrackedEvents(TrackingToken lastToken, int batchSize) {
        if (lastToken instanceof AggregateTrackingToken) {
            AggregateTrackingToken token = (AggregateTrackingToken) lastToken;
            return fetchDomainEvents(token.getAggregateIdentifier(), token.getSequenceNumber(), batchSize);
        } else if (lastToken instanceof GlobalIndexTrackingToken) {
            GlobalIndexTrackingToken token = (GlobalIndexTrackingToken) lastToken;
            ResultSet resultSet = session.execute("SELECT " + quoted(domainEventFields()) +
                            " FROM " + quoted(schema().domainEventTable()) +
                            " WHERE " + quoted("index") + " = ?" +
                            " AND " + quoted(schema().globalIndexColumn()) + " > ?" +
                            " ORDER BY " + quoted(schema().globalIndexColumn()) + " ASC" +
                            " LIMIT ?",
                    INDEX,
                    token.getGlobalIndex(),
                    batchSize);
            return eventMapper.map(resultSet).all();
        } else if (lastToken instanceof LegacyTrackingToken) {
            LegacyTrackingToken token = (LegacyTrackingToken) lastToken;
            ResultSet resultSet = session.execute("SELECT " + quoted(domainEventFields()) +
                            " FROM " + quoted(schema().domainEventTable()) +
                            " WHERE " + quoted("index") + " = ?" +
                            " AND ((" + quoted(schema().timestampColumn()) + " > ?) " +
                            " OR (" + quoted(schema().timestampColumn()) + " = ? AND " + quoted(schema().sequenceNumberColumn()) + " > ?) " +
                            " OR (" + quoted(schema().timestampColumn()) + " = ? AND " + quoted(schema().sequenceNumberColumn()) + " = ? AND " + quoted(schema().aggregateIdentifierColumn()) + " > ?))" +
                            " ORDER BY " +
                            quoted(schema().timestampColumn()) + " ASC " +
                            quoted(schema().sequenceNumberColumn()) + " ASC " +
                            quoted(schema().aggregateIdentifierColumn()) + " ASC " +
                            " LIMIT ?" +
                            " ALLOW FILTERING",
                    INDEX,
                    token.getTimestamp(),
                    token.getTimestamp(),
                    token.getSequenceNumber(),
                    token.getTimestamp(),
                    token.getSequenceNumber(),
                    token.getAggregateIdentifier(),
                    batchSize);
            return eventMapper.map(resultSet).all();
        } else {
            throw new IllegalArgumentException(String.format("Token %s is of the wrong type", lastToken));
        }
    }

    @Override
    protected List<DomainEventEntry> fetchDomainEvents(String aggregateIdentifier, long firstSequenceNumber, int batchSize) {
        ResultSet resultSet = session.execute("SELECT " + quoted(domainEventFields()) +
                        " FROM " + quoted(schema().domainEventTable()) +
                        " WHERE " + quoted(schema().aggregateIdentifierColumn()) + " = ?" +
                        " AND " + quoted(schema().sequenceNumberColumn()) + " >= ?" +
                        " ORDER BY " + quoted(schema().sequenceNumberColumn()) + " ASC" +
                        " LIMIT ?",
                aggregateIdentifier,
                firstSequenceNumber,
                batchSize);
        return eventMapper.map(resultSet).all();
    }

    @Override
    protected TrackingToken getTokenForGapDetection(TrackingToken token) {
        return token;
    }

    @Override
    protected void appendEvents(List<? extends EventMessage<?>> events, Serializer serializer) {
        events.stream()
                .map(EventUtils::asDomainEventMessage)
                .map(e -> asDomainEventEntry(e, serializer))
                .forEach(e -> {
                    eventMapper.save(e);
                    addToBatch(e.getAggregateIdentifier(), e.getSequenceNumber());
                });
    }

    private DomainEventEntry asDomainEventEntry(DomainEventMessage<?> eventMessage, Serializer serializer) {
        long globalIndex = incrementCounter("globalIndex");
        return new DomainEventEntry(INDEX, globalIndex, eventMessage, serializer);
    }

    private long incrementCounter(String column) {
        session.execute("UPDATE " + quoted("Indexes") +
                " SET " + quoted(column) + " = " + quoted(column) + " + 1" +
                " WHERE " + quoted("index") + " = ?", INDEX);
        Row row = session.execute("SELECT " + quoted(column) + " FROM " + quoted("Indexes")).one();
        return row.getLong(0);
    }

    private static SnapshotEventEntry asSnapshotEventEntry(DomainEventMessage<?> eventMessage, Serializer serializer) {
        return new SnapshotEventEntry(eventMessage, serializer);
    }

    private void addToBatch(String aggregateIdentifier, long sequenceNumber) {
        UnitOfWork<?> root = CurrentUnitOfWork.get().root();
        List<TupleValue> tuples = root.getOrComputeResource("batch", s -> {
            List<TupleValue> result = new ArrayList<>();
            root.onCommit(unitOfWork -> storeBatch(result));
            return result;
        });
        tuples.add(batchEventType.newValue(aggregateIdentifier, sequenceNumber));
    }

    private void storeBatch(List<TupleValue> tuples) {
        long batchIndex = incrementCounter("batchIndex");
        batchMapper.save(new BatchEntry(batchIndex, tuples));
    }

    @Override
    protected void storeSnapshot(DomainEventMessage<?> snapshot, Serializer serializer) {
        snapshotMapper.save(asSnapshotEventEntry(snapshot, serializer));
    }

    @Override
    protected Optional<? extends DomainEventData<?>> readSnapshotData(String aggregateIdentifier) {
        return Optional.ofNullable(snapshotMapper.get(aggregateIdentifier));
    }

    protected List<String> domainEventFields() {
        return Arrays.asList(schema().eventIdentifierColumn(), schema().timestampColumn(), schema().payloadTypeColumn(),
                schema().payloadRevisionColumn(), schema().payloadColumn(), schema().metaDataColumn(),
                schema().typeColumn(), schema().aggregateIdentifierColumn(), schema().sequenceNumberColumn());
    }

    protected EventSchema schema() {
        return schema;
    }

    private String quoted(String input) {
        return '"' + input + '"';
    }

    private String quoted(Iterable<String> input) {
        return quoted(String.join("\", \"", input));
    }
}
