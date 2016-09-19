package nl.ordina.personen.cassandra;

import com.datastax.driver.core.*;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import org.axonframework.common.transaction.TransactionManager;
import org.axonframework.eventhandling.EventMessage;
import org.axonframework.eventsourcing.DomainEventMessage;
import org.axonframework.eventsourcing.eventstore.BatchingEventStorageEngine;
import org.axonframework.eventsourcing.eventstore.DomainEventData;
import org.axonframework.eventsourcing.eventstore.TrackingToken;
import org.axonframework.eventsourcing.eventstore.jdbc.EventSchema;
import org.axonframework.serialization.Serializer;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by gle21221 on 2-9-2016.
 */
public class CassandraReadOnlyEventStorageEngine extends BatchingEventStorageEngine {

    private final EventSchema schema;
    final Session session;
    final Mapper<DomainEventEntry> eventMapper;
    final Mapper<SnapshotEventEntry> snapshotMapper;
    final Mapper<TransactionEntry> transactionMapper;
    final TupleType eventsReferencesType;

    public CassandraReadOnlyEventStorageEngine(Session session, TransactionManager transactionManager) {
        this(session, transactionManager, CassandraEventSchema.builder().build());
    }

    public CassandraReadOnlyEventStorageEngine(Session session, TransactionManager transactionManager, EventSchema schema) {
        super(transactionManager);
        this.session = session;
        this.schema = schema;
        MappingManager mappingManager = new MappingManager(session);
        this.eventMapper = mappingManager.mapper(DomainEventEntry.class);
        this.snapshotMapper = mappingManager.mapper(SnapshotEventEntry.class);
        this.transactionMapper = mappingManager.mapper(TransactionEntry.class);
        Metadata metadata = session.getCluster().getMetadata();
        this.eventsReferencesType = metadata.newTupleType(DataType.varchar(), DataType.bigint(), DataType.bigint());
    }

    @Override
    protected List<DomainEventEntry> fetchTrackedEvents(TrackingToken lastToken, int batchSize) {
        if (lastToken == null || lastToken instanceof TransactionTrackingToken) {
            long lastTransactionIndex = lastToken != null ? ((TransactionTrackingToken) lastToken).getTransactionIndex() : 0;
            long nextTransactionIndex = lastTransactionIndex + 1;
            Optional<TransactionEntry> transaction = Optional.ofNullable(transactionMapper.get(nextTransactionIndex));
            return transaction
                    .map(t -> t.eventsReferences()
                            .flatMap(eventsReference -> fetchReferencedEvents(eventsReference, batchSize).stream())
                            .collect(Collectors.toList())
                    ).orElse(Collections.emptyList());
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
        throw new UnsupportedOperationException();
    }

    @Override
    protected void storeSnapshot(DomainEventMessage<?> snapshot, Serializer serializer) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Optional<? extends DomainEventData<?>> readSnapshotData(String aggregateIdentifier) {
        return Optional.ofNullable(snapshotMapper.get(aggregateIdentifier));
    }

    private List<DomainEventEntry> fetchReferencedEvents(EventsReference eventsReference, int batchSize) {
        ResultSet resultSet = session.execute("SELECT " + quoted(domainEventFields()) +
                        " FROM " + quoted(schema().domainEventTable()) +
                        " WHERE " + quoted(schema().aggregateIdentifierColumn()) + " = ?" +
                        " AND " + quoted(schema().sequenceNumberColumn()) + " >= ?" +
                        " AND " + quoted(schema().sequenceNumberColumn()) + " <= ?" +
                        " ORDER BY " + quoted(schema().sequenceNumberColumn()) + " ASC" +
                        " LIMIT ?",
                eventsReference.getAggregateIdentifier(),
                eventsReference.getFirstSequenceNumber(),
                eventsReference.getLastSequenceNumber(),
                batchSize);
        return eventMapper.map(resultSet).all();
    }

    private EventSchema schema() {
        return schema;
    }

    private List<String> domainEventFields() {
        return Arrays.asList(schema().eventIdentifierColumn(), schema().timestampColumn(), schema().payloadTypeColumn(),
                schema().payloadRevisionColumn(), schema().payloadColumn(), schema().metaDataColumn(),
                schema().typeColumn(), schema().aggregateIdentifierColumn(), schema().sequenceNumberColumn(),
                "transactionIndex");
    }

    static String quoted(String input) {
        return '"' + input + '"';
    }

    static String quoted(Iterable<String> input) {
        return quoted(String.join("\", \"", input));
    }
}
