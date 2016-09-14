package nl.ordina.personen

import com.datastax.driver.core.Cluster
import nl.ordina.personen.datatype._
import nl.ordina.personen.datatype.groep.{Geboorte, Overlijden}
import nl.ordina.personen.event.cassandra.{CassandraClient, CassandraEventStorageEngine, CassandraTransactionManager}
import org.axonframework.eventsourcing.eventstore.EmbeddedEventStore

package object event {

  private lazy val cluster = Cluster.builder().addContactPoint("127.0.0.1").build()
  private lazy val session = cluster.connect("brp")
  private lazy val client = new CassandraClient(session)
  lazy val transactionManager = new CassandraTransactionManager(client)
  private lazy val eventStorageEngine = new CassandraEventStorageEngine(transactionManager, client)
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
