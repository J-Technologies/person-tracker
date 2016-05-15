/**
  * Copyright (C) 2016 Ordina
  */

package nl.ordina

import org.axonframework.scynapse.annotations.aggregateId

package object commands {

  case class CreateToDoItemCommand(@aggregateId todoId: String, description: String)
  case class MarkCompletedCommand(@aggregateId todoId: String)

}

package object events {

  case class ToDoItemCreatedEvent(todoId: String, description: String)
  case class ToDoItemCompletedEvent(todoId: String)

}
