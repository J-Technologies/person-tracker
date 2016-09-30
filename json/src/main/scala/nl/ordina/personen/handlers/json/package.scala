package nl.ordina.personen.handlers

import com.datastax.driver.core.Cluster
import com.datastax.driver.mapping.MappingManager
import nl.ordina.personen.event
import org.axonframework.cassandra.eventsourcing.eventstore.CassandraTokenStore
import org.axonframework.serialization.xml.XStreamSerializer

/**
  * Created by gle21221 on 5-8-2016.
  */
package object json {

  private val cassandraHost = Option(System.getenv("CASSANDRA_HOST")).getOrElse("127.0.0.1")
  private val cluster = Cluster.builder().addContactPoint(cassandraHost).build()
  event.createSchemaIfNotExists(cluster, "brp")

  private lazy val session = cluster.connect("brp")
  private lazy val serializer = new XStreamSerializer()
  lazy val tokenStore = new CassandraTokenStore(session, serializer)
  lazy val persoonMapper = new MappingManager(session).mapper(classOf[PersoonEntry])

}
