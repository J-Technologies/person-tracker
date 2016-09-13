package nl.ordina.personen.handlers

import javax.persistence.Persistence

import nl.ordina.personen.event
import nl.ordina.personen.event.cassandra.CassandraTokenStore
import org.axonframework.common.jpa.SimpleEntityManagerProvider
import org.axonframework.serialization.xml.XStreamSerializer

/**
  * Created by gle21221 on 5-8-2016.
  */
package object json {

  private lazy val entityManagerFactory = Persistence.createEntityManagerFactory("JSON")
  private lazy val entityManagerProvider = new SimpleEntityManagerProvider(entityManagerFactory.createEntityManager())
  private lazy val serializer = new XStreamSerializer()
  private lazy val transactionManager = JpaTransactionManager(entityManagerProvider)
  lazy val tokenStore = new CassandraTokenStore(event.session, serializer)
  lazy val jsonRepository = JsonRepository(entityManagerProvider, transactionManager)

}
