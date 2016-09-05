package nl.ordina.personen.handlers.json

import org.axonframework.common.jpa.EntityManagerProvider
import org.axonframework.common.transaction.TransactionManager
import org.axonframework.eventhandling.tokenstore.jpa.JpaTokenStore
import org.axonframework.eventsourcing.eventstore.TrackingToken
import org.axonframework.serialization.Serializer

/**
  * Created by gle21221 on 5-8-2016.
  */
object JpaTransactionTokenStore {
  def apply(entityManagerProvider: EntityManagerProvider, serializer: Serializer, transactionManager: TransactionManager): JpaTokenStore =
    new JpaTokenStore(entityManagerProvider, serializer) {
      override def storeToken(processName: String, segment: Int, token: TrackingToken): Unit = {
        val transaction = transactionManager.startTransaction()
        super.storeToken(processName, segment, token)
        transaction.commit()
      }
    }
}
