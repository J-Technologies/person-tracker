package nl.ordina.personen.event.cassandra;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;
import com.datastax.driver.mapping.annotations.Transient;
import org.axonframework.eventsourcing.DomainEventMessage;
import org.axonframework.eventsourcing.eventstore.DomainEventData;
import org.axonframework.eventsourcing.eventstore.GlobalIndexTrackingToken;
import org.axonframework.eventsourcing.eventstore.TrackedEventData;
import org.axonframework.eventsourcing.eventstore.TrackingToken;
import org.axonframework.serialization.*;

import java.nio.ByteBuffer;
import java.time.Instant;
import java.util.Date;

/**
 * Created by gle21221 on 2-9-2016.
 */
@Table(name = "DomainEventEntry", caseSensitiveTable = true)
public class DomainEventEntry implements DomainEventData<byte[]>, TrackedEventData<byte[]> {

    @PartitionKey
    @Column(caseSensitive = true)
    private String index;
    @Column(caseSensitive = true)
    private String eventIdentifier;
    @Column(caseSensitive = true)
    private Date timeStamp;
    @Column(caseSensitive = true)
    private String payloadType;
    @Column(caseSensitive = true)
    private String payloadRevision;
    @Column(caseSensitive = true)
    private ByteBuffer payload;
    @Column(caseSensitive = true)
    private ByteBuffer metaData;
    @Column(caseSensitive = true)
    private String type;
    @Column(caseSensitive = true)
    @PartitionKey(1)
    private String aggregateIdentifier;
    @Column(caseSensitive = true)
    @PartitionKey(2)
    private long sequenceNumber;
    @Column(caseSensitive = true)
    @PartitionKey(3)
    private long globalIndex;

    public DomainEventEntry(String index, long globalIndex, DomainEventMessage<?> eventMessage, Serializer serializer) {
        SerializedObject<byte[]> payload = serializer.serialize(eventMessage.getPayload(), byte[].class);
        SerializedObject<byte[]> metaData = serializer.serialize(eventMessage.getMetaData(), byte[].class);
        this.index = index;
        this.globalIndex = globalIndex;
        this.eventIdentifier = eventMessage.getIdentifier();
        this.payloadType = payload.getType().getName();
        this.payloadRevision = payload.getType().getRevision();
        this.payload = ByteBuffer.wrap(payload.getData());
        this.metaData = ByteBuffer.wrap(metaData.getData());
        this.timeStamp = new Date(eventMessage.getTimestamp().toEpochMilli());
        this.type = eventMessage.getType();
        this.aggregateIdentifier = eventMessage.getAggregateIdentifier();
        this.sequenceNumber = eventMessage.getSequenceNumber();
    }

    protected DomainEventEntry() {
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public String getAggregateIdentifier() {
        return aggregateIdentifier;
    }

    @Override
    public long getSequenceNumber() {
        return sequenceNumber;
    }

    @Override
    public TrackingToken trackingToken() {
//        return new AggregateTrackingToken(aggregateIdentifier, sequenceNumber);
        return new GlobalIndexTrackingToken(globalIndex);
//        return new LegacyTrackingToken(getTimestamp(), aggregateIdentifier, sequenceNumber);
    }

    @Override
    public String getEventIdentifier() {
        return eventIdentifier;
    }

    @Override
    @Transient
    public Instant getTimestamp() {
        return timeStamp.toInstant();
    }

    @Override
    @Transient
    @SuppressWarnings("unchecked")
    public SerializedObject<byte[]> getMetaData() {
        return new SerializedMetaData<>(metaData.array(), byte[].class);
    }

    @Override
    @Transient
    @SuppressWarnings("unchecked")
    public SerializedObject<byte[]> getPayload() {
        return new SimpleSerializedObject<>(payload.array(), byte[].class, getPayloadType());
    }

    public String getIndex() {
        return index;
    }

    public String getPayloadRevision() {
        return payloadRevision;
    }

    public long getGlobalIndex() {
        return globalIndex;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public void setEventIdentifier(String eventIdentifier) {
        this.eventIdentifier = eventIdentifier;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public void setPayloadType(String payloadType) {
        this.payloadType = payloadType;
    }

    public void setPayloadRevision(String payloadRevision) {
        this.payloadRevision = payloadRevision;
    }

    public void setPayload(ByteBuffer payload) {
        this.payload = payload;
    }

    public void setMetaData(ByteBuffer metaData) {
        this.metaData = metaData;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setAggregateIdentifier(String aggregateIdentifier) {
        this.aggregateIdentifier = aggregateIdentifier;
    }

    public void setSequenceNumber(long sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public void setGlobalIndex(long globalIndex) {
        this.globalIndex = globalIndex;
    }

    protected SerializedType getPayloadType() {
        return new SimpleSerializedType(payloadType, payloadRevision);
    }
}
