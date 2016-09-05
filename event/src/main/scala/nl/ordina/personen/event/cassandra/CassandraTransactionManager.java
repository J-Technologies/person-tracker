package nl.ordina.personen.event.cassandra;

import com.datastax.driver.core.Session;
import org.axonframework.common.transaction.Transaction;
import org.axonframework.common.transaction.TransactionIsolationLevel;
import org.axonframework.common.transaction.TransactionManager;

/**
 * Created by gle21221 on 2-9-2016.
 */
public class CassandraTransactionManager implements TransactionManager {

    private final Session session;

    public CassandraTransactionManager(Session session) {
        this.session = session;
    }

    @Override
    public Transaction startTransaction(TransactionIsolationLevel isolationLevel) {
        return new Transaction() {
            @Override
            public void commit() {

            }

            @Override
            public void rollback() {

            }
        };
    }
}
