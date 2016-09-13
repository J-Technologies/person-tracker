package nl.ordina.personen.event.cassandra;

import org.axonframework.common.transaction.Transaction;
import org.axonframework.common.transaction.TransactionIsolationLevel;
import org.axonframework.common.transaction.TransactionManager;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by gle21221 on 2-9-2016.
 */
public class CassandraTransactionManager implements TransactionManager {

    private final CassandraClient client;
    private final AtomicLong transactionCounter = new AtomicLong();
    private final AtomicLong eventCounter = new AtomicLong();
    private CassandraTransaction currentTransaction;

    public CassandraTransactionManager(CassandraClient client) {
        this.client = client;
        this.transactionCounter.set(client.selectCounter("transactions"));
        this.eventCounter.set(client.selectCounter("eventCounter"));
    }

    @Override
    public Transaction startTransaction(TransactionIsolationLevel isolationLevel) {
        if (currentTransaction != null && !currentTransaction.isCompleted()) {
            throw new IllegalStateException("A transaction is already in progress!");
        }
        currentTransaction = new CassandraTransaction(client, transactionCounter, eventCounter);
        return currentTransaction;
    }

    public void add(DomainEventEntry event) {
        if (currentTransaction != null) {
            currentTransaction.add(event);
        }
    }

    public void add(SnapshotEventEntry snapshot) {
        if (currentTransaction != null) {
            currentTransaction.add(snapshot);
        }
    }

}
