package nl.ordina.personen.event.cassandra;

import org.axonframework.eventsourcing.eventstore.TrackingToken;

import java.util.Objects;

/**
 * Created by gle21221 on 3-9-2016.
 */
public class BatchTrackingToken implements TrackingToken {

    private final long batchIndex;
    private final int eventIndex;

    public BatchTrackingToken(long batchIndex, int eventIndex) {
        this.batchIndex = batchIndex;
        this.eventIndex = eventIndex;
    }

    @Override
    public boolean isGuaranteedNext(TrackingToken otherToken) {
        BatchTrackingToken that = (BatchTrackingToken) otherToken;
        return this.batchIndex == that.batchIndex && this.eventIndex - that.eventIndex == 1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BatchTrackingToken that = (BatchTrackingToken) o;

        return this.batchIndex == that.batchIndex && this.eventIndex == that.eventIndex;

    }

    @Override
    public int hashCode() {
        return Objects.hash(batchIndex, eventIndex);
    }

    @Override
    public int compareTo(TrackingToken o) {
        BatchTrackingToken that = (BatchTrackingToken) o;
        int result = Long.compare(this.batchIndex, that.batchIndex);
        if (result == 0) result = Long.compare(this.eventIndex, that.eventIndex);
        return result;
    }
}
