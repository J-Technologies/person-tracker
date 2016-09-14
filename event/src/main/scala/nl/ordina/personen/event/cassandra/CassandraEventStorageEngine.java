package nl.ordina.personen.event.cassandra;

import org.axonframework.common.transaction.TransactionManager;
import org.axonframework.eventhandling.EventMessage;
import org.axonframework.eventsourcing.DomainEventMessage;
import org.axonframework.eventsourcing.eventstore.BatchingEventStorageEngine;
import org.axonframework.eventsourcing.eventstore.DomainEventData;
import org.axonframework.eventsourcing.eventstore.EventUtils;
import org.axonframework.eventsourcing.eventstore.TrackingToken;
import org.axonframework.serialization.Serializer;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by gle21221 on 2-9-2016.
 */
public class CassandraEventStorageEngine extends BatchingEventStorageEngine {

    private final TransactionManager transactionManager;
    private final CassandraClient client;

    public CassandraEventStorageEngine(TransactionManager transactionManager, CassandraClient client) {
        super(transactionManager);
        this.transactionManager = transactionManager;
        this.client = client;
    }

    @Override
    protected List<DomainEventEntry> fetchTrackedEvents(TrackingToken lastToken, int batchSize) {
        if (lastToken == null || lastToken instanceof TransactionTrackingToken) {
            long lastTransactionIndex = lastToken != null ? ((TransactionTrackingToken) lastToken).getTransactionIndex() : 0;
            long nextTransactionIndex = lastTransactionIndex + 1;
            Optional<TransactionEntry> transaction = Optional.ofNullable(client.select(nextTransactionIndex));
            return transaction.map(t -> t.eventsReferences()
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
        events.stream()
                .map(EventUtils::asDomainEventMessage)
                .map(e -> asDomainEventEntry(e, serializer))
                .forEach(this::save);
    }

    @Override
    protected void storeSnapshot(DomainEventMessage<?> snapshot, Serializer serializer) {
        save(asSnapshotEventEntry(snapshot, serializer));
    }

    @Override
    protected Optional<? extends DomainEventData<?>> readSnapshotData(String aggregateIdentifier) {
        return Optional.ofNullable(client.select(aggregateIdentifier));
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
