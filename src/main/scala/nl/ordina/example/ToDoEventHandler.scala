/**
  * Copyright (C) 2016 Ordina
  */

package nl.ordina.example

import nl.ordina.events._
import org.axonframework.eventhandling.annotation.EventHandler

import scala.collection.mutable

class ToDoEventHandler {

  val queue = new mutable.Queue[String]()

  @EventHandler
  def handle(event: ToDoItemCreatedEvent) {
    queue += s"Received: ${event.description} -|> ${event.todoId}"
  }

  @EventHandler
  def handle(event: ToDoItemCompletedEvent) {
    println(s"Completed: ${event.todoId}")
  }
}
