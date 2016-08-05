package nl.ordina.personen.handlers.json

import org.axonframework.common.jpa.EntityManagerProvider
import org.axonframework.common.transaction.TransactionManager

/**
  * Created by gle21221 on 5-8-2016.
  */
class JsonRepository(entityManagerProvider: EntityManagerProvider, transactionManager: TransactionManager) {
  def select(bsn: String): PersoonEntry =
    entityManagerProvider.getEntityManager.createQuery(
      "SELECT NEW nl.ordina.personen.handlers.json.PersoonEntry(bsn, naam, isOverleden) " +
        "FROM " + persoonEntryName + " WHERE bsn = :bsn",
      classOf[PersoonEntry])
      .setParameter("bsn", bsn)
      .setMaxResults(1)
      .getResultList.stream().findFirst().orElse(null)

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