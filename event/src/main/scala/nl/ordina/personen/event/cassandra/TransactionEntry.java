package nl.ordina.personen.event.cassandra;

import com.datastax.driver.core.TupleType;
import com.datastax.driver.core.TupleValue;
import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by gle21221 on 13-9-2016.
 */
@Table(name = "TransactionEntry", caseSensitiveTable = true)
public class TransactionEntry {
    @Column(caseSensitive = true)
    @PartitionKey
    private long transactionIndex;
    @Column(caseSensitive = true)
    private List<TupleValue> eventsReferences;

    public TransactionEntry(long transactionIndex, Stream<EventsReference> eventsReferences, TupleType eventsReferencesType) {
        this.transactionIndex = transactionIndex;
        this.eventsReferences = eventsReferences
                .map(eventsReference -> eventsReferencesType.newValue(
                        eventsReference.getAggregateIdentifier(),
                        eventsReference.getFirstSequenceNumber(),
                        eventsReference.getLastSequenceNumber())
                )
                .collect(Collectors.toList());
    }

    protected TransactionEntry() {
    }

    Stream<EventsReference> eventsReferences() {
        return Optional.ofNullable(eventsReferences)
                .map(tupleValues -> tupleValues.stream()
                        .map(tupleValue -> new EventsReference(
                                tupleValue.getString(0),
                                tupleValue.getLong(1),
                                tupleValue.getLong(2))
                        )
                ).orElse(Stream.empty());
    }
}
