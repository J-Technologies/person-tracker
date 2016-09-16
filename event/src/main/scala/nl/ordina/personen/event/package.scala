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

  private lazy val session = cluster.connect("axon")
  lazy val transactionManager = NoTransactionManager.INSTANCE
  private lazy val eventStorageEngine = new CassandraEventStorageEngine(session, transactionManager)
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

  case class HuwelijkGecreeërd()
}
