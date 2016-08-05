package nl.ordina.personen.event

import javax.persistence.EntityTransaction

import org.axonframework.common.jpa.EntityManagerProvider
import org.axonframework.common.transaction.{Transaction, TransactionIsolationLevel, TransactionManager}

/**
  * Created by gle21221 on 8-7-2016.
  */
class JpaTransactionManager(entityManagerProvider: EntityManagerProvider) extends TransactionManager {
  override def startTransaction(isolationLevel: TransactionIsolationLevel): Transaction = new JpaTransaction(entityManagerProvider.getEntityManager.getTransaction)
}

class JpaTransaction(entityTransaction: EntityTransaction) extends Transaction {
  entityTransaction.begin()

  override def rollback(): Unit = entityTransaction.rollback()

  override def commit(): Unit = entityTransaction.commit()
}
