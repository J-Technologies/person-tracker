package nl.ordina.personen

import javax.persistence.Persistence

import nl.ordina.personen.datatype._
import nl.ordina.personen.datatype.groep.{Geboorte, Overlijden}
import org.axonframework.common.jpa.SimpleEntityManagerProvider
import org.axonframework.eventsourcing.eventstore.EmbeddedEventStore
import org.axonframework.eventsourcing.eventstore.jpa.JpaEventStorageEngine
import org.axonframework.serialization.xml.XStreamSerializer

package object event {

  private lazy val entityManagerFactory = Persistence.createEntityManagerFactory("BRP")
  private lazy val entityManagerProvider = new SimpleEntityManagerProvider(entityManagerFactory.createEntityManager())
  private lazy val serializer = new XStreamSerializer()
  private lazy val eventStorageEngine = new JpaEventStorageEngine(entityManagerProvider, transactionManager) {
    setSerializer(serializer)
  }
  lazy val transactionManager = JpaTransactionManager(entityManagerProvider)
  lazy val eventStore = new EmbeddedEventStore(eventStorageEngine)

  case class PersoonGeboren(
    bsn: Burgerservicenummer,
    naam: SamengesteldeNaam,
    geslacht: Geslachtsaanduiding,
    geboorte: Geboorte,
    bijhoudingspartij: Partij
  )

  case class PersoonOverleden(
    bsn: Burgerservicenummer,
    overlijden: Overlijden
  )
}
