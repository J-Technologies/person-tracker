/**
  * Copyright (C) 2016 Ordina
  */

package nl.ordina.example

import nl.ordina.commands.{CreateToDoItemCommand, MarkCompletedCommand}
import nl.ordina.events.{ToDoItemCompletedEvent, ToDoItemCreatedEvent}
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.commandhandling.model.AggregateLifecycle.apply
import org.axonframework.commandhandling.model.AggregateRoot
import org.axonframework.eventhandling.EventHandler
import org.axonframework.eventsourcing.AggregateIdentifier

@AggregateRoot
class ToDoItem {

  @AggregateIdentifier
  private var id: String = _

  @CommandHandler
  def this(command: CreateToDoItemCommand) {
    this()
    apply(new ToDoItemCreatedEvent(command.todoId, command.description))
  }

  @EventHandler
  def on(e: ToDoItemCreatedEvent) = {
    id = e.todoId
  }

  @CommandHandler
  def markCompleted(command: MarkCompletedCommand) {
    apply(new ToDoItemCompletedEvent(id))
  }
}

