package nl.ordina.personen.event.cassandra;

import org.axonframework.common.transaction.Transaction;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by gle21221 on 10-9-2016.
 */
class CassandraTransaction implements Transaction {

    private final CassandraClient client;
    private final AtomicLong transactionCounter;
    private final AtomicLong eventCounter;
    private final List<DomainEventEntry> events = new LinkedList<>();
    private final List<SnapshotEventEntry> snapshots = new LinkedList<>();
    private final Map<String, EventsReference> eventsReferences = new TreeMap<>();
    private boolean committed, rolledBack;
    private boolean completed;

    CassandraTransaction(CassandraClient client, AtomicLong transactionCounter, AtomicLong eventCounter) {
        this.client = client;
        this.transactionCounter = transactionCounter;
        this.eventCounter = eventCounter;
    }

    public boolean isCommitted() {
        return committed;
    }

    public boolean isRolledBack() {
        return rolledBack;
    }

    public boolean isCompleted() {
        return isCommitted() || isRolledBack();
    }

    void add(DomainEventEntry event) {
        events.add(event);
        EventsReference eventsReference = eventsReferences.computeIfAbsent(event.getAggregateIdentifier(),
                aggregateIdentifier -> new EventsReference(aggregateIdentifier, event.getSequenceNumber()));
        eventsReference.update(event.getSequenceNumber());
    }

    void add(SnapshotEventEntry snapshot) {
        snapshots.add(snapshot);
    }

    @Override
    public void commit() {
        long transactionIndex = transactionCounter.incrementAndGet();
        long eventCount = eventCounter.addAndGet(events.size());
        client.insert(new TransactionEntry(transactionIndex, eventsReferences.values().stream(), client.getEventsReferencesType()));
        events.forEach(event -> {
            event.setTransactionIndex(transactionIndex);
            client.insert(event);
        });
        snapshots.forEach(client::insert);
        client.updateCounter("transactions", transactionIndex);
        client.updateCounter("events", eventCount);
        committed = true;
    }

    @Override
    public void rollback() {
        events.clear();
        snapshots.clear();
        eventsReferences.clear();
        rolledBack = true;
    }
}
