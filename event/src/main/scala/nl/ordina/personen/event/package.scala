package nl.ordina.personen

import com.datastax.driver.core.Cluster
import nl.ordina.personen.datatype._
import nl.ordina.personen.datatype.groep.{Geboorte, Overlijden}
import nl.ordina.personen.event.cassandra.{CassandraStorageEngine, CassandraTransactionManager}
import org.axonframework.eventsourcing.eventstore.EmbeddedEventStore

package object event {

  private lazy val cluster = Cluster.builder().addContactPoint("127.0.0.1").build()
  private lazy val session = cluster.connect("brp")
  lazy val transactionManager = new CassandraTransactionManager(session)
  private lazy val eventStorageEngine = new CassandraStorageEngine(transactionManager, session)
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
