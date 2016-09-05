package nl.ordina.personen.event.cassandra;

import org.axonframework.eventsourcing.eventstore.TrackingToken;

import java.util.Objects;

/**
 * Created by gle21221 on 3-9-2016.
 */
public class AggregateTrackingToken implements TrackingToken {

    private final String aggregateIdentifier;
    private final long sequenceNumber;

    public AggregateTrackingToken(String aggregateIdentifier, long sequenceNumber) {
        this.aggregateIdentifier = aggregateIdentifier;
        this.sequenceNumber = sequenceNumber;
    }

    public String getAggregateIdentifier() {
        return aggregateIdentifier;
    }

    public long getSequenceNumber() {
        return sequenceNumber;
    }

    @Override
    public boolean isGuaranteedNext(TrackingToken otherToken) {
        AggregateTrackingToken that = (AggregateTrackingToken) otherToken;
        return Objects.equals(this.aggregateIdentifier, that.aggregateIdentifier) && this.sequenceNumber - that.sequenceNumber == 1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AggregateTrackingToken that = (AggregateTrackingToken) o;

        return Objects.equals(this.aggregateIdentifier, that.aggregateIdentifier) && this.sequenceNumber == that.sequenceNumber;

    }

    @Override
    public int hashCode() {
        return Objects.hash(aggregateIdentifier, sequenceNumber);
    }

    @Override
    public int compareTo(TrackingToken o) {
        AggregateTrackingToken that = (AggregateTrackingToken) o;
        int result = this.aggregateIdentifier.compareTo(that.aggregateIdentifier);
        if (result == 0) result = Long.compare(this.sequenceNumber, that.sequenceNumber);
        return result;
    }
}
