package nl.ordina.personen.event.cassandra;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.TupleType;
import com.datastax.driver.core.TupleValue;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import org.axonframework.common.transaction.TransactionManager;
import org.axonframework.eventhandling.EventMessage;
import org.axonframework.eventsourcing.DomainEventMessage;
import org.axonframework.eventsourcing.eventstore.BatchingEventStorageEngine;
import org.axonframework.eventsourcing.eventstore.DomainEventData;
import org.axonframework.eventsourcing.eventstore.EventUtils;
import org.axonframework.eventsourcing.eventstore.TrackingToken;
import org.axonframework.eventsourcing.eventstore.jdbc.EventSchema;
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
        if (!(lastToken != null && lastToken instanceof AggregateTrackingToken)) {
            throw new IllegalArgumentException(String.format("Token %s is of the wrong type", lastToken));
        }
        AggregateTrackingToken token = (AggregateTrackingToken) lastToken;
        return fetchDomainEvents(token.getAggregateIdentifier(), token.getSequenceNumber(), batchSize);
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
                .forEach(eventMapper::save);
    }

    private static DomainEventEntry asDomainEventEntry(DomainEventMessage<?> eventMessage, Serializer serializer) {
        return new DomainEventEntry(eventMessage, serializer);
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
        batchMapper.save(new BatchEntry(1, tuples));
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
