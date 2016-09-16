package nl.ordina.personen.cassandra;

import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.Statement;
import org.axonframework.common.transaction.TransactionManager;
import org.axonframework.eventhandling.EventMessage;
import org.axonframework.eventsourcing.DomainEventMessage;
import org.axonframework.eventsourcing.eventstore.EventUtils;
import org.axonframework.eventsourcing.eventstore.jdbc.EventSchema;
import org.axonframework.messaging.unitofwork.CurrentUnitOfWork;
import org.axonframework.messaging.unitofwork.UnitOfWork;
import org.axonframework.serialization.Serializer;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by gle21221 on 2-9-2016.
 */
public class CassandraEventStorageEngine extends CassandraReadOnlyEventStorageEngine {

    final String referencesKey = this + "_REFERENCES";
    final String statementsKey = this + "_STATEMENTS";

    private final AtomicLong transactionCounter;

    public CassandraEventStorageEngine(Session session, TransactionManager transactionManager) {
        this(session, transactionManager, CassandraEventSchema.builder().build());
    }

    public CassandraEventStorageEngine(Session session, TransactionManager transactionManager, EventSchema schema) {
        super(session, transactionManager, schema);
        this.transactionCounter = new AtomicLong(selectCounter("transactions"));
    }

    @Override
    protected void appendEvents(List<? extends EventMessage<?>> events, Serializer serializer) {
        events.stream()
                .map(EventUtils::asDomainEventMessage)
                .forEachOrdered(this::addReference);
        events.stream()
                .map(EventUtils::asDomainEventMessage)
                .map(e -> asDomainEventEntry(e, serializer, transactionCounter.get()))
                .map(eventMapper::saveQuery)
                .forEachOrdered(statements()::add);
    }

    @Override
    protected void storeSnapshot(DomainEventMessage<?> snapshot, Serializer serializer) {
        statements().add(snapshotMapper.saveQuery(asSnapshotEventEntry(snapshot, serializer)));
    }

    private void addReference(DomainEventMessage<?> event) {
        references()
                .computeIfAbsent(event.getAggregateIdentifier(), s -> new EventsReference(s, event.getSequenceNumber()))
                .update(event.getSequenceNumber());
    }

    private Map<String, EventsReference> references() {
        UnitOfWork<?> root = CurrentUnitOfWork.get().root();
        return root.getOrComputeResource(referencesKey, s -> {
            Map<String, EventsReference> result = new TreeMap<>();
            root.onPrepareCommit(this::onPrepareCommitStoreTransaction);
            return result;
        });
    }

    private List<Statement> statements() {
        UnitOfWork<?> root = CurrentUnitOfWork.get().root();
        return root.getOrComputeResource(statementsKey, s -> {
            List<Statement> result = new ArrayList<>();
            root.onCommit(this::onCommitExecuteStatements);
            return result;
        });
    }

    private void onPrepareCommitStoreTransaction(UnitOfWork<?> unitOfWork) {
        Map<String, EventsReference> references = unitOfWork.getResource(referencesKey);
        if (references != null) {
            long transactionIndex = transactionCounter.getAndIncrement();
            TransactionEntry transaction = new TransactionEntry(transactionIndex, references.values().stream(), eventsReferencesType);
            statements().addAll(Arrays.asList(transactionMapper.saveQuery(transaction), updateCounter("transactions", transactionIndex)));
        }
    }

    private void onCommitExecuteStatements(UnitOfWork<?> unitOfWork) {
        List<Statement> statements = unitOfWork.getResource(statementsKey);
        if (statements != null) {
            statements.forEach(session::execute);
        }
    }

    private long selectCounter(String name) {
        ResultSet resultSet = session.execute("SELECT " + quoted("value") +
                " FROM" + quoted("Counters") +
                " WHERE " + quoted("name") + " = ? LIMIT 1", name);
        return Optional.ofNullable(resultSet.one()).map(row -> row.getLong(0)).orElse(1L);
    }

    private Statement updateCounter(String name, long value) {
        PreparedStatement result = session.prepare("UPDATE " + quoted("Counters") +
                " SET " + quoted("value") + " = ?" +
                " WHERE " + quoted("name") + " = ?");
        return result.bind(value, name);
    }

    private static DomainEventEntry asDomainEventEntry(DomainEventMessage<?> eventMessage, Serializer serializer, long transactionIndex) {
        return new DomainEventEntry(eventMessage, serializer, transactionIndex);
    }

    private static SnapshotEventEntry asSnapshotEventEntry(DomainEventMessage<?> eventMessage, Serializer serializer) {
        return new SnapshotEventEntry(eventMessage, serializer);
    }

}
