package nl.ordina.personen

import com.datastax.driver.core.Cluster
import nl.ordina.personen.cassandra.CassandraEventStorageEngine
import nl.ordina.personen.datatype._
import nl.ordina.personen.datatype.groep.{Geboorte, Overlijden}
import org.axonframework.common.transaction.NoTransactionManager
import org.axonframework.eventsourcing.eventstore.EmbeddedEventStore

package object event {

  private val cluster = Cluster.builder().addContactPoint("127.0.0.1").build()
  cassandra.createSchemaIfNotExists(cluster, "axon")
  lazy val transactionManager = NoTransactionManager.INSTANCE
  private lazy val eventStorageEngine = new CassandraEventStorageEngine(cluster.connect("axon"), transactionManager)
  lazy val eventStore = new EmbeddedEventStore(eventStorageEngine)

  case class PersoonGeboren(
    bsn: Burgerservicenummer,
    naam: SamengesteldeNaam,
    geslacht: Geslachtsaanduiding,
    geboorte: Geboorte,
    ouders: List[Burgerservicenummer],
    adres: Adres,
    bijhoudingspartij: Partij
  )

  case class PersoonOverleden(
    bsn: Burgerservicenummer,
    overlijden: Overlijden
  )

  case class HuwelijkGecreeërd(
    bsn: Burgerservicenummer,
    partner: Burgerservicenummer,
    datum: Datum,
    gemeente: Gemeente
  )

}
