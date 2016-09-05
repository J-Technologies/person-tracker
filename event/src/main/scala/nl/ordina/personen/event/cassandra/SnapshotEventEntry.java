package nl.ordina.personen.event.cassandra;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;
import com.datastax.driver.mapping.annotations.Transient;
import org.axonframework.eventsourcing.DomainEventMessage;
import org.axonframework.eventsourcing.eventstore.DomainEventData;
import org.axonframework.serialization.*;

import java.nio.ByteBuffer;
import java.time.Instant;

/**
 * Created by gle21221 on 2-9-2016.
 */
@Table(name = "SnapshotEventEntry", caseSensitiveTable = true)
public class SnapshotEventEntry implements DomainEventData<byte[]> {

    @Column(caseSensitive = true)
    private String eventIdentifier;
    @Column(caseSensitive = true)
    private String timeStamp;
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
    @PartitionKey
    private String aggregateIdentifier;
    @Column(caseSensitive = true)
    private long sequenceNumber;

    public SnapshotEventEntry(DomainEventMessage<?> eventMessage, Serializer serializer) {
        SerializedObject<byte[]> payload = serializer.serialize(eventMessage.getPayload(), byte[].class);
        SerializedObject<byte[]> metaData = serializer.serialize(eventMessage.getMetaData(), byte[].class);
        this.eventIdentifier = eventMessage.getIdentifier();
        this.payloadType = payload.getType().getName();
        this.payloadRevision = payload.getType().getRevision();
        this.payload = ByteBuffer.wrap(payload.getData());
        this.metaData = ByteBuffer.wrap(metaData.getData());
        this.timeStamp = eventMessage.getTimestamp().toString();
        this.type = eventMessage.getType();
        this.aggregateIdentifier = eventMessage.getAggregateIdentifier();
        this.sequenceNumber = eventMessage.getSequenceNumber();
    }

    protected SnapshotEventEntry() {
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
    public String getEventIdentifier() {
        return eventIdentifier;
    }

    @Override
    @Transient
    public Instant getTimestamp() {
        return Instant.parse(timeStamp);
    }

    @Override
    @SuppressWarnings("unchecked")
    public SerializedObject<byte[]> getMetaData() {
        return new SerializedMetaData<>(metaData.array(), byte[].class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public SerializedObject<byte[]> getPayload() {
        return new SimpleSerializedObject<>(payload.array(), byte[].class, getPayloadType());
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public String getPayloadRevision() {
        return payloadRevision;
    }

    public void setEventIdentifier(String eventIdentifier) {
        this.eventIdentifier = eventIdentifier;
    }

    public void setTimeStamp(String timeStamp) {
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

    protected SerializedType getPayloadType() {
        return new SimpleSerializedType(payloadType, payloadRevision);
    }
}
