package nl.ordina.personen.handlers

import com.datastax.driver.core.Cluster
import com.datastax.driver.mapping.MappingManager
import nl.ordina.personen.cassandra
import nl.ordina.personen.cassandra.CassandraTokenStore
import org.axonframework.serialization.xml.XStreamSerializer

/**
  * Created by gle21221 on 5-8-2016.
  */
package object json {

  lazy val tokenStore = new CassandraTokenStore(session, serializer)
  cassandra.createSchemaIfNotExists(cluster, "json")
  lazy val persoonMapper = new MappingManager(session).mapper(classOf[PersoonEntry])
  private lazy val session = cluster.connect("json")
  private lazy val serializer = new XStreamSerializer()
  private val cluster = Cluster.builder().addContactPoint("127.0.0.1").build()

}
