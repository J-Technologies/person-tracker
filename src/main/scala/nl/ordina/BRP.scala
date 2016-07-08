package nl.ordina

import javax.persistence.Persistence

import nl.ordina.http.WebServer
import nl.ordina.personen.domein.entity.NatuurlijkPersoon
import org.axonframework.commandhandling.gateway.DefaultCommandGateway
import org.axonframework.commandhandling.{AggregateAnnotationCommandHandler, SimpleCommandBus}
import org.axonframework.common.jpa.SimpleEntityManagerProvider
import org.axonframework.eventsourcing.EventSourcingRepository
import org.axonframework.eventsourcing.eventstore.EmbeddedEventStore
import org.axonframework.eventsourcing.eventstore.jpa.JpaEventStorageEngine
import org.axonframework.serialization.xml.XStreamSerializer

object BRP {

  val entityManagerFactory = Persistence.createEntityManagerFactory("Axon")
  val entityManagerProvider = new SimpleEntityManagerProvider(entityManagerFactory.createEntityManager())
  val transactionManager = new JpaTransactionManager(entityManagerProvider)
  val eventStorageEngine = new JpaEventStorageEngine(entityManagerProvider, transactionManager)
  val eventStore = new EmbeddedEventStore(eventStorageEngine)
  val repository = new EventSourcingRepository[NatuurlijkPersoon](classOf[NatuurlijkPersoon], eventStore)
  val commandBus = new SimpleCommandBus()
  val commandGateway = new DefaultCommandGateway(commandBus)
  val queueEventHandler = new QueueEventHandler(eventStore)
  val webServer = new WebServer(eventStore, commandGateway)

  def setupAxon() = {
    eventStorageEngine.setSerializer(new XStreamSerializer())
    commandBus.setTransactionManager(transactionManager)
    new AggregateAnnotationCommandHandler(classOf[NatuurlijkPersoon], repository).subscribe(commandBus)
  }

  def main(args: Array[String]) {
    val commandHandlerRegistration = setupAxon()
    webServer.start()
    commandHandlerRegistration.close()
  }
}
