package nl.ordina.personen

import com.datastax.driver.core.Cluster
import nl.ordina.personen.datatype._
import nl.ordina.personen.datatype.groep.{Geboorte, Overlijden}
import org.axonframework.cassandra.eventsourcing.eventstore.{CassandraEventSchema, CassandraEventStorageEngine}
import org.axonframework.common.jdbc.PersistenceExceptionResolver
import org.axonframework.common.transaction.NoTransactionManager
import org.axonframework.eventsourcing.eventstore.EmbeddedEventStore
import org.axonframework.serialization.upcasting.event.NoOpEventUpcasterChain
import org.axonframework.serialization.xml.XStreamSerializer

import scala.io.Source

package object event {

  private val cassandraHost = Option(System.getenv("CASSANDRA_HOST")).getOrElse("127.0.0.1")
  private val cluster = Cluster.builder().addContactPoint(cassandraHost).build()
  createSchemaIfNotExists(cluster, "axon")
  lazy val transactionManager = NoTransactionManager.INSTANCE
  private lazy val eventStorageEngine = new CassandraEventStorageEngine(
    new XStreamSerializer(),
    NoOpEventUpcasterChain.INSTANCE,
    new PersistenceExceptionResolver {
      override def isDuplicateKeyViolation(exception: Exception): Boolean = false
    },
    transactionManager,
    100,
    cluster.connect("axon"),
    CassandraEventSchema.builder().build()
  )
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

  def createSchemaIfNotExists(cluster: Cluster, keyspace: String): Unit = {
    val source = Source.fromURL(getClass.getResource("/cql/" + keyspace + ".cql"))
    val script = source.mkString
    source.close()

    val statements = script.split(";").transform(s => s.trim).filter(s => s.nonEmpty)

    val session = cluster.newSession()
    session
      .execute("CREATE KEYSPACE IF NOT EXISTS \"" + keyspace + "\" WITH REPLICATION = { 'class' : " +
        "'NetworkTopologyStrategy', 'datacenter1' : 1 };")
    session.execute("USE \"" + keyspace + "\";")
    statements.foreach(s => session.execute(s))
    session.close()
  }
}
