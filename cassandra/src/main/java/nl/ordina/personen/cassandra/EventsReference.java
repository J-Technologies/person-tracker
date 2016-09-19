package nl.ordina.personen.cassandra;

/**
 * Created by gle21221 on 10-9-2016.
 */
public class EventsReference implements Comparable<EventsReference> {
    private final String aggregateIdentifier;
    private long firstSequenceNumber;
    private long lastSequenceNumber;

    public EventsReference(String aggregateIdentifier, long sequenceNumber) {
        this(aggregateIdentifier, sequenceNumber, sequenceNumber);
    }

    public EventsReference(String aggregateIdentifier, long firstSequenceNumber, long lastSequenceNumber) {
        this.aggregateIdentifier = aggregateIdentifier;
        this.firstSequenceNumber = firstSequenceNumber;
        this.lastSequenceNumber = lastSequenceNumber;
    }

    public String getAggregateIdentifier() {
        return aggregateIdentifier;
    }

    public long getFirstSequenceNumber() {
        return firstSequenceNumber;
    }

    public long getLastSequenceNumber() {
        return lastSequenceNumber;
    }

    void update(long sequenceNumber) {
        if (sequenceNumber < firstSequenceNumber) {
            firstSequenceNumber = sequenceNumber;
        }
        if (sequenceNumber > lastSequenceNumber) {
            lastSequenceNumber = sequenceNumber;
        }
    }

    @Override
    public int compareTo(EventsReference o) {
        int result = aggregateIdentifier.compareTo(o.aggregateIdentifier);
        if (result == 0) {
            result = Long.compare(firstSequenceNumber, o.firstSequenceNumber);
        }
        if (result == 0) {
            result = Long.compare(lastSequenceNumber, o.lastSequenceNumber);
        }
        return result;
    }

    @Override
    public int hashCode() {
        int result = aggregateIdentifier.hashCode();
        result = 31 * result + (int) (firstSequenceNumber ^ (firstSequenceNumber >>> 32));
        result = 31 * result + (int) (lastSequenceNumber ^ (lastSequenceNumber >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EventsReference that = (EventsReference) o;

        return aggregateIdentifier.equals(that.aggregateIdentifier) &&
                firstSequenceNumber == that.firstSequenceNumber &&
                lastSequenceNumber == that.lastSequenceNumber;
    }

    @Override
    public String toString() {
        return "EventsReference{" +
                "aggregateIdentifier='" + aggregateIdentifier + '\'' +
                ", firstSequenceNumber=" + firstSequenceNumber +
                ", lastSequenceNumber=" + lastSequenceNumber +
                '}';
    }
}
