package nl.ordina.personen.event.cassandra;

import org.axonframework.common.transaction.TransactionManager;
import org.axonframework.eventhandling.EventMessage;
import org.axonframework.eventsourcing.DomainEventMessage;
import org.axonframework.eventsourcing.eventstore.EventUtils;
import org.axonframework.serialization.Serializer;

import java.util.List;

/**
 * Created by gle21221 on 2-9-2016.
 */
public class CassandraEventStorageEngine extends CassandraReadOnlyEventStorageEngine {

    private final TransactionManager transactionManager;
    private final CassandraClient client;

    public CassandraEventStorageEngine(TransactionManager transactionManager, CassandraClient client) {
        super(transactionManager, client);
        this.transactionManager = transactionManager;
        this.client = client;
    }

    @Override
    protected void appendEvents(List<? extends EventMessage<?>> events, Serializer serializer) {
        events.stream()
                .map(EventUtils::asDomainEventMessage)
                .map(e -> asDomainEventEntry(e, serializer))
                .forEach(this::save);
    }

    @Override
    protected void storeSnapshot(DomainEventMessage<?> snapshot, Serializer serializer) {
        save(asSnapshotEventEntry(snapshot, serializer));
    }

    private void save(DomainEventEntry event) {
        if (transactionManager instanceof CassandraTransactionManager) {
            ((CassandraTransactionManager) transactionManager).add(event);
        } else {
            client.insert(event);
        }
    }

    private void save(SnapshotEventEntry snapshot) {
        if (transactionManager instanceof CassandraTransactionManager) {
            ((CassandraTransactionManager) transactionManager).add(snapshot);
        } else {
            client.insert(snapshot);
        }
    }

    private static DomainEventEntry asDomainEventEntry(DomainEventMessage<?> eventMessage, Serializer serializer) {
        return new DomainEventEntry(eventMessage, serializer);
    }

    private static SnapshotEventEntry asSnapshotEventEntry(DomainEventMessage<?> eventMessage, Serializer serializer) {
        return new SnapshotEventEntry(eventMessage, serializer);
    }

}
