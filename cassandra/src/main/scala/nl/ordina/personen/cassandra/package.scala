package nl.ordina.personen

import com.datastax.driver.core.Cluster

import scala.io.Source

/**
  * Created by gle21221 on 16-9-2016.
  */
package object cassandra {

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
