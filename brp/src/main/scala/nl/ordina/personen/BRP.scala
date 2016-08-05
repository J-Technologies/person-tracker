package nl.ordina.personen

import nl.ordina.personen.domein.entity.NatuurlijkPersoon
import org.axonframework.commandhandling.gateway.DefaultCommandGateway
import org.axonframework.commandhandling.{AggregateAnnotationCommandHandler, SimpleCommandBus}
import org.axonframework.eventsourcing.EventSourcingRepository

object BRP {

  val repository = new EventSourcingRepository[NatuurlijkPersoon](classOf[NatuurlijkPersoon], event.eventStore)
  val commandBus = new SimpleCommandBus() {
    setTransactionManager(event.transactionManager)
  }
  val commandGateway = new DefaultCommandGateway(commandBus)
  val commandHandler = new AggregateAnnotationCommandHandler(classOf[NatuurlijkPersoon], repository)
  val webServer = new WebServer(commandGateway)

  def main(args: Array[String]) {
    val commandHandlerRegistration = commandHandler.subscribe(commandBus)
    webServer.start()
    commandHandlerRegistration.close()
  }
}
