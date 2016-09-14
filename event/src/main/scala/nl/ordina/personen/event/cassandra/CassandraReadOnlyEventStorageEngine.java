package nl.ordina.personen.event.cassandra;

import org.axonframework.common.transaction.TransactionManager;
import org.axonframework.eventhandling.EventMessage;
import org.axonframework.eventsourcing.DomainEventMessage;
import org.axonframework.eventsourcing.eventstore.BatchingEventStorageEngine;
import org.axonframework.eventsourcing.eventstore.DomainEventData;
import org.axonframework.eventsourcing.eventstore.TrackingToken;
import org.axonframework.serialization.Serializer;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by gle21221 on 2-9-2016.
 */
public class CassandraReadOnlyEventStorageEngine extends BatchingEventStorageEngine {

    private final CassandraClient client;

    public CassandraReadOnlyEventStorageEngine(TransactionManager transactionManager, CassandraClient client) {
        super(transactionManager);
        this.client = client;
    }

    @Override
    protected List<DomainEventEntry> fetchTrackedEvents(TrackingToken lastToken, int batchSize) {
        if (lastToken == null || lastToken instanceof TransactionTrackingToken) {
            long lastTransactionIndex = lastToken != null ? ((TransactionTrackingToken) lastToken).getTransactionIndex() : 0;
            long nextTransactionIndex = lastTransactionIndex + 1;
            Optional<TransactionEntry> transaction = Optional.ofNullable(client.select(nextTransactionIndex));
            return transaction
                    .map(t -> t.eventsReferences()
                            .flatMap(eventsReference -> client.select(eventsReference, batchSize).stream())
                            .collect(Collectors.toList())
                    ).orElse(Collections.emptyList());
        } else {
            throw new IllegalArgumentException(String.format("Token %s is of the wrong type", lastToken));
        }
    }

    @Override
    protected List<DomainEventEntry> fetchDomainEvents(String aggregateIdentifier, long firstSequenceNumber, int batchSize) {
        return client.select(aggregateIdentifier, firstSequenceNumber, batchSize);
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
        return Optional.ofNullable(client.select(aggregateIdentifier));
    }
}
