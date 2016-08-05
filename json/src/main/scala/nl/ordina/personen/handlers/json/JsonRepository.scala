package nl.ordina.personen.handlers.json

import org.axonframework.common.jpa.EntityManagerProvider
import org.axonframework.common.transaction.TransactionManager
import scala.collection.JavaConverters._


class JsonRepository(entityManagerProvider: EntityManagerProvider, transactionManager: TransactionManager) {
  def select(bsn: String): Option[PersoonEntry] =
    entityManagerProvider.getEntityManager.createQuery(
      "SELECT NEW nl.ordina.personen.handlers.json.PersoonEntry(bsn, naam, isOverleden) " +
        "FROM " + persoonEntryName + " WHERE bsn = :bsn",
      classOf[PersoonEntry])
      .setParameter("bsn", bsn)
      .getResultList.asScala.headOption


  def store(persoonEntry: PersoonEntry): Unit = {
    val entityManager = entityManagerProvider.getEntityManager
    val transaction = transactionManager.startTransaction()
    entityManager.persist(persoonEntry)
    entityManager.flush()
    transaction.commit()
  }

  def persoonEntryName: String = classOf[PersoonEntry].getSimpleName
}

object JsonRepository {
  def apply(entityManagerProvider: EntityManagerProvider, transactionManager: TransactionManager): JsonRepository =
    new JsonRepository(entityManagerProvider, transactionManager)
}