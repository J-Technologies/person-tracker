package nl.ordina.personen.event.cassandra;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;
import org.axonframework.eventsourcing.eventstore.TrackingToken;
import org.axonframework.serialization.SerializedObject;
import org.axonframework.serialization.Serializer;
import org.axonframework.serialization.SimpleSerializedObject;
import org.axonframework.serialization.SimpleSerializedType;

import java.nio.ByteBuffer;
import java.time.Instant;

/**
 * Created by gle21221 on 2-9-2016.
 */
@Table(name = "TokenEntry", caseSensitiveTable = true)
public class TokenEntry {
    @Column(caseSensitive = true)
    @PartitionKey
    private String processName;
    @Column(caseSensitive = true)
    @PartitionKey(1)
    private int segment;
    @Column(caseSensitive = true)
    private ByteBuffer token;
    @Column(caseSensitive = true)
    private String tokenType;
    @Column(caseSensitive = true)
    private long timeStamp;

    public TokenEntry(String process, int segment, TrackingToken token, Serializer serializer) {
        SerializedObject<byte[]> serializedToken = serializer.serialize(token, byte[].class);
        this.processName = process;
        this.segment = segment;
        this.token = ByteBuffer.wrap(serializedToken.getData());
        this.tokenType = serializedToken.getType().getName();
        this.timeStamp = Instant.now().toEpochMilli();
    }

    protected TokenEntry() {
    }

    public String getProcessName() {
        return processName;
    }

    public int getSegment() {
        return segment;
    }

    public ByteBuffer getToken() {
        return token;
    }

    public String getTokenType() {
        return tokenType;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public void setSegment(int segment) {
        this.segment = segment;
    }

    public void setToken(ByteBuffer token) {
        this.token = token;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public TrackingToken trackingToken(Serializer serializer) {
        SimpleSerializedObject<byte[]> serializedObject =
                new SimpleSerializedObject<>(token.array(), byte[].class, new SimpleSerializedType(tokenType, null));
        return serializer.deserialize(serializedObject);
    }
}
