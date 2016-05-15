/**
  * Copyright (C) 2016 Ordina
  */

package nl.ordina.example

import nl.ordina.events._
import org.axonframework.eventhandling.annotation.EventHandler

class ToDoEventHandler {

  @EventHandler
  def handle(event: ToDoItemCreatedEvent) {
    println(s"Received: ${event.description} -|> ${event.todoId}")
  }

  @EventHandler
  def handle(event: ToDoItemCompletedEvent) {
    println(s"Completed: ${event.todoId}")
  }
}
