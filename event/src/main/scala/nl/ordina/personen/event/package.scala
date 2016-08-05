package nl.ordina.personen

import javax.persistence.Persistence

import nl.ordina.personen.datatype._
import nl.ordina.personen.datatype.groep.{Geboorte, Overlijden}
import org.axonframework.common.jpa.SimpleEntityManagerProvider
import org.axonframework.eventhandling.tokenstore.jpa.JpaTokenStore
import org.axonframework.eventsourcing.eventstore.EmbeddedEventStore
import org.axonframework.eventsourcing.eventstore.jpa.JpaEventStorageEngine
import org.axonframework.serialization.xml.XStreamSerializer

package object event {

  private lazy val entityManagerFactory = Persistence.createEntityManagerFactory("Axon")
  lazy val entityManagerProvider = new SimpleEntityManagerProvider(entityManagerFactory.createEntityManager())
  lazy val serializer = new XStreamSerializer()
  lazy val transactionManager = new JpaTransactionManager(entityManagerProvider)
  lazy val eventStorageEngine = new JpaEventStorageEngine(entityManagerProvider, transactionManager) {
    setSerializer(serializer)
  }
  lazy val eventStore = new EmbeddedEventStore(eventStorageEngine)
  lazy val tokenStore = new JpaTokenStore(entityManagerProvider, serializer)

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
