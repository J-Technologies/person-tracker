package nl.ordina.personen.event.cassandra;

import com.datastax.driver.core.*;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import org.axonframework.eventsourcing.eventstore.jdbc.EventSchema;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Created by gle21221 on 13-9-2016.
 */
public class CassandraClient {
    private final Session session;
    private final EventSchema schema;
    private final Mapper<DomainEventEntry> eventMapper;
    private final Mapper<SnapshotEventEntry> snapshotMapper;
    private final Mapper<TransactionEntry> transactionMapper;
    private final TupleType eventsReferencesType;

    public CassandraClient(Session session) {
        this(session, CassandraEventSchema.builder().build());
    }

    public CassandraClient(Session session, EventSchema schema) {
        this.session = session;
        this.schema = schema;
        MappingManager mappingManager = new MappingManager(session);
        this.eventMapper = mappingManager.mapper(DomainEventEntry.class);
        this.snapshotMapper = mappingManager.mapper(SnapshotEventEntry.class);
        this.transactionMapper = mappingManager.mapper(TransactionEntry.class);
        Metadata metadata = session.getCluster().getMetadata();
        this.eventsReferencesType = metadata.newTupleType(DataType.varchar(), DataType.bigint(), DataType.bigint());
    }

    public Session getSession() {
        return session;
    }

    public TupleType getEventsReferencesType() {
        return eventsReferencesType;
    }

    public List<DomainEventEntry> select(String aggregateIdentifier, long firstSequenceNumber, int batchSize) {
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

    public List<DomainEventEntry> select(EventsReference eventsReference, int batchSize) {
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

    public SnapshotEventEntry select(String aggregateIdentifier) {
        return snapshotMapper.get(aggregateIdentifier);
    }

    public TransactionEntry select(long transactionIndex) {
        return transactionMapper.get(transactionIndex);
    }

    public void insert(DomainEventEntry event) {
        eventMapper.save(event);
    }

    public void insert(SnapshotEventEntry snapshot) {
        snapshotMapper.save(snapshot);
    }

    public void insert(TransactionEntry transaction) {
        transactionMapper.save(transaction);
    }

    public long selectCounter(String name) {
        return Optional.ofNullable(session.execute("SELECT " + quoted("value") +
                " FROM" + quoted("Counters") +
                " WHERE " + quoted("name") + " = ? LIMIT 1", name)
                .one()).map(row -> row.getLong(0)).orElse(0L);
    }

    public void updateCounter(String name, long value) {
        session.execute("UPDATE " + quoted("Counters") +
                        " SET " + quoted("value") + " = ?" +
                        " WHERE " + quoted("name") + " = ?",
                value,
                name);
    }

    protected List<String> domainEventFields() {
        return Arrays.asList(schema().eventIdentifierColumn(), schema().timestampColumn(), schema().payloadTypeColumn(),
                schema().payloadRevisionColumn(), schema().payloadColumn(), schema().metaDataColumn(),
                schema().typeColumn(), schema().aggregateIdentifierColumn(), schema().sequenceNumberColumn(),
                "transactionIndex");
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
