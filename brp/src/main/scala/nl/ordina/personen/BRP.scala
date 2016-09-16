package nl.ordina.personen

import nl.ordina.personen.domein.entity.NatuurlijkPersoon
import org.axonframework.commandhandling.gateway.DefaultCommandGateway
import org.axonframework.commandhandling.{AggregateAnnotationCommandHandler, SimpleCommandBus}
import org.axonframework.eventsourcing.EventSourcingRepository

object BRP {

  def main(args: Array[String]) {
    val repository = new EventSourcingRepository[NatuurlijkPersoon](classOf[NatuurlijkPersoon], event.eventStore)
    val commandBus = new SimpleCommandBus() {
      setTransactionManager(event.transactionManager)
    }
    val commandGateway = new DefaultCommandGateway(commandBus)
    val commandHandler = new AggregateAnnotationCommandHandler(classOf[NatuurlijkPersoon], repository)
    val commandHandlerRegistration = commandHandler.subscribe(commandBus)

    new WebServer(commandGateway).start()
    commandHandlerRegistration.close()
  }
}
