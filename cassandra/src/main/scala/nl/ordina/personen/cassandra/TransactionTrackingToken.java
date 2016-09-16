package nl.ordina.personen.cassandra;

import org.axonframework.eventsourcing.eventstore.TrackingToken;

/**
 * Created by gle21221 on 3-9-2016.
 */
public class TransactionTrackingToken implements TrackingToken {

    private final long transactionIndex;

    public TransactionTrackingToken(long transactionIndex) {
        this.transactionIndex = transactionIndex;
    }

    public long getTransactionIndex() {
        return transactionIndex;
    }

    @Override
    public boolean isGuaranteedNext(TrackingToken otherToken) {
        TransactionTrackingToken that = (TransactionTrackingToken) otherToken;
        return that.transactionIndex - this.transactionIndex == 1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TransactionTrackingToken that = (TransactionTrackingToken) o;
        return this.transactionIndex == that.transactionIndex;

    }

    @Override
    public int hashCode() {
        return Long.hashCode(transactionIndex);
    }

    @Override
    public int compareTo(TrackingToken o) {
        TransactionTrackingToken that = (TransactionTrackingToken) o;
        return Long.compare(this.transactionIndex, that.transactionIndex);
    }
}
