package nl.ordina.example

import nl.ordina.commands.{CreateToDoItemCommand, MarkCompletedCommand}
import nl.ordina.events.{ToDoItemCompletedEvent, ToDoItemCreatedEvent}
import org.axonframework.commandhandling.annotation.CommandHandler
import org.axonframework.eventhandling.annotation.EventHandler
import org.axonframework.eventsourcing.annotation.{AbstractAnnotatedAggregateRoot, AggregateIdentifier}

object Domain {

  class ToDoItem extends AbstractAnnotatedAggregateRoot[String] {

    @AggregateIdentifier
    private var id : String = _

    @CommandHandler
    def this(command : CreateToDoItemCommand) {
      this()
      apply(new ToDoItemCreatedEvent(command.todoId, command.description))
    }

    @EventHandler
    def on(e: ToDoItemCreatedEvent) = {
      id = e.todoId
    }

    @CommandHandler
    def markCompleted(command : MarkCompletedCommand) {
      apply(new ToDoItemCompletedEvent(id))
    }
  }
}
