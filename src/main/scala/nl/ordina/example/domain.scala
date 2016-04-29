package nl.ordina.example

import org.axonframework.commandhandling.annotation.CommandHandler
import org.axonframework.eventhandling.annotation.EventHandler
import org.axonframework.eventsourcing.annotation.{AbstractAnnotatedAggregateRoot, AggregateIdentifier}
import org.axonframework.scynapse.annotations._

object Domain {

  class ToDoEventHandler {
    @EventHandler
    def handle(event: ToDoItemCreatedEvent) = print("We've got something to do: " + event.description + " (" + event.todoId + ")")

    @EventHandler
    def handle(event: ToDoItemCompletedEvent) = print("We've completed a task: " + event.todoId)
  }

  class ToDoItem extends AbstractAnnotatedAggregateRoot[String] {

    @AggregateIdentifier
    private var id: String = _

    @CommandHandler
    def handleCreate(cmd: CreateToDoItemCommand) {
      apply(ToDoItemCreatedEvent(cmd.todoId, cmd.description))
    }

    @EventHandler
    def onCreate(e: ToDoItemCreatedEvent) = {
      id = e.todoId
    }

    @CommandHandler
    def handleMarkdown(cmd: MarkCompletedCommand) {
      apply(ToDoItemCompletedEvent(cmd.todoId))
    }

    @EventHandler
    def onMarkdown(e: ToDoItemCompletedEvent) = {
      id = e.todoId
    }
  }

  case class CreateToDoItemCommand(@aggregateId todoId: String, description: String)

  case class ToDoItemCreatedEvent(todoId: String, description: String)

  case class MarkCompletedCommand(@aggregateId todoId: String)

  case class ToDoItemCompletedEvent(todoId: String)

}
