package nl.ordina.personen.event.cassandra;

import com.datastax.driver.core.TupleValue;
import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;
import com.datastax.driver.mapping.annotations.Transient;

import java.util.List;
import java.util.Optional;

/**
 * Created by gle21221 on 3-9-2016.
 */
@Table(name = "BatchEntry", caseSensitiveTable = true)
public class BatchEntry {
    @Column(caseSensitive = true)
    @PartitionKey
    private long batchIndex;
    @Column(caseSensitive = true)
    private List<TupleValue> events;

    public BatchEntry(long batchIndex, List<TupleValue> events) {
        this.batchIndex = batchIndex;
        this.events = events;
    }

    protected BatchEntry() {

    }

    public long getBatchIndex() {
        return batchIndex;
    }

    public List<TupleValue> getEvents() {
        return events;
    }

    public void setBatchIndex(long batchIndex) {
        this.batchIndex = batchIndex;
    }

    public void setEvents(List<TupleValue> events) {
        this.events = events;
    }

    @Transient
    public Optional<Integer> getEventIndex(String aggregateIdentifier, long sequenceNumber) {
        Optional<TupleValue> e = events.stream().filter(t -> t.getString(1).equals(aggregateIdentifier) && t.getLong(2) == sequenceNumber).findFirst();
        return e.map(t -> events.indexOf(t));
    }
}
