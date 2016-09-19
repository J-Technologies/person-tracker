package nl.ordina.personen.cassandra;

import com.datastax.driver.core.BatchStatement;
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
    final String batchKey = this + "_BATCH";

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
                .forEachOrdered(batch()::add);
    }

    @Override
    protected void storeSnapshot(DomainEventMessage<?> snapshot, Serializer serializer) {
         batch().add(snapshotMapper.saveQuery(asSnapshotEventEntry(snapshot, serializer)));
    }

    private BatchStatement batch() {
        UnitOfWork<?> root = CurrentUnitOfWork.get().root();
        return root.getOrComputeResource(batchKey, s -> {
            BatchStatement batch = new BatchStatement();
            root.onCommit(unitOfWork -> session.execute(batch));
            return batch;
        });
    }

    private Map<String, EventsReference> references() {
        UnitOfWork<?> root = CurrentUnitOfWork.get().root();
        return root.getOrComputeResource(referencesKey, s -> {
            Map<String, EventsReference> result = new TreeMap<>();
            root.onPrepareCommit(unitOfWork -> {
                TransactionEntry transaction = new TransactionEntry(transactionCounter.get(), result.values().stream(), eventsReferencesType);
                batch().add(updateCounter("transactions", transaction.getTransactionIndex()));
                batch().add(session.prepare("INSERT INTO " + quoted("TransactionEntry") +
                        " (" + quoted("transactionIndex", "eventsReference") + ")" +
                        " VALUES(?,?)").bind(transaction.getTransactionIndex(), transaction.getEventsReferences()));
            });
            root.afterCommit(unitOfWork -> transactionCounter.incrementAndGet());
            return result;
        });
    }

    private void addReference(DomainEventMessage<?> event) {
        references()
                .computeIfAbsent(event.getAggregateIdentifier(), s -> new EventsReference(s, event.getSequenceNumber()))
                .update(event.getSequenceNumber());
    }

    private long selectCounter(String name) {
        ResultSet resultSet = session.execute("SELECT " + quoted("value") +
                " FROM" + quoted("Counters") +
                " WHERE " + quoted("name") + " = ? LIMIT 1", name);
        return Optional.ofNullable(resultSet.one()).map(row -> row.getLong(0)).orElse(1L);
    }

    private Statement updateCounter(String name, long value) {
        return session.prepare("UPDATE " + quoted("Counters") +
                " SET " + quoted("value") + " = ?" +
                " WHERE " + quoted("name") + " = ?").bind(value, name);
    }

    private static DomainEventEntry asDomainEventEntry(DomainEventMessage<?> eventMessage, Serializer serializer, long transactionIndex) {
        return new DomainEventEntry(eventMessage, serializer, transactionIndex);
    }

    private static SnapshotEventEntry asSnapshotEventEntry(DomainEventMessage<?> eventMessage, Serializer serializer) {
        return new SnapshotEventEntry(eventMessage, serializer);
    }

}
