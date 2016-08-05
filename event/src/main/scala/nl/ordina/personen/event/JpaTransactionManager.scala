package nl.ordina.personen.event

import org.axonframework.common.jpa.EntityManagerProvider
import org.axonframework.common.transaction.{Transaction, TransactionIsolationLevel, TransactionManager}

object JpaTransactionManager {
  def apply(entityManagerProvider: EntityManagerProvider): TransactionManager = {
    new TransactionManager() {
      override def startTransaction(isolationLevel: TransactionIsolationLevel): Transaction =
        new Transaction {
          val entityTransaction = entityManagerProvider.getEntityManager.getTransaction
          entityTransaction.begin()

          override def rollback(): Unit = entityTransaction.rollback()

          override def commit(): Unit = entityTransaction.commit()
        }
    }
  }
}