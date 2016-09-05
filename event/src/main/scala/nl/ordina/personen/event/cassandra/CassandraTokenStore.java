package nl.ordina.personen.event.cassandra;

import com.datastax.driver.core.Session;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import org.axonframework.common.transaction.Transaction;
import org.axonframework.common.transaction.TransactionManager;
import org.axonframework.eventhandling.tokenstore.TokenStore;
import org.axonframework.eventsourcing.eventstore.TrackingToken;
import org.axonframework.serialization.Serializer;

import java.util.Optional;

/**
 * Created by gle21221 on 2-9-2016.
 */
public class CassandraTokenStore implements TokenStore {

    private final TransactionManager transactionManager;
    private final Serializer serializer;
    private final Mapper<TokenEntry> tokenMapper;

    public CassandraTokenStore(TransactionManager transactionManager, Serializer serializer, Session session) {
        this.transactionManager = transactionManager;
        this.serializer = serializer;
        MappingManager mappingManager = new MappingManager(session);
        this.tokenMapper = mappingManager.mapper(TokenEntry.class);
    }

    @Override
    public void storeToken(String processName, int segment, TrackingToken token) {
        Transaction transaction = transactionManager.startTransaction();
        try {
            tokenMapper.save(new TokenEntry(processName, segment, token, serializer));
            transaction.commit();
        } catch (Throwable e) {
            transaction.rollback();
            throw e;
        }
    }

    @Override
    public TrackingToken fetchToken(String processName, int segment) {
        Optional<TokenEntry> tokenEntry = Optional.ofNullable(tokenMapper.get(processName, segment));
        return tokenEntry.map(entry -> entry.trackingToken(serializer)).orElse(null);
    }
}
