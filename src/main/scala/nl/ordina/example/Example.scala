package nl.ordina.example

import java.io.File
import java.util.UUID

import nl.ordina.example.Domain.{CreateToDoItemCommand, MarkCompletedCommand, ToDoEventHandler, ToDoItem}
import org.axonframework.commandhandling.SimpleCommandBus
import org.axonframework.commandhandling.annotation.AggregateAnnotationCommandHandler
import org.axonframework.commandhandling.gateway.DefaultCommandGateway
import org.axonframework.eventhandling.SimpleEventBus
import org.axonframework.eventhandling.annotation.AnnotationEventListenerAdapter
import org.axonframework.eventsourcing.EventSourcingRepository
import org.axonframework.eventstore.fs.{FileSystemEventStore, SimpleEventFileResolver}

object Example {

  def run() = {
    val commandBus = new SimpleCommandBus()
    val commandGateway = new DefaultCommandGateway(commandBus)

    val eventStore = new FileSystemEventStore(new SimpleEventFileResolver(new File("./events")))

    val eventBus = new SimpleEventBus()

    AnnotationEventListenerAdapter.subscribe(new ToDoEventHandler(), eventBus)

    val repo = new EventSourcingRepository[ToDoItem](classOf[ToDoItem], eventStore)
    repo.setEventBus(eventBus)

    AggregateAnnotationCommandHandler.subscribe(classOf[ToDoItem], repo, commandBus)

    val itemId = UUID.randomUUID().toString
    commandGateway.send(new CreateToDoItemCommand(itemId, "Need to do this"))
    commandGateway.send(new MarkCompletedCommand(itemId))
  }

}
