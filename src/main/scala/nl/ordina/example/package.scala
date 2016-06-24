/**
  * Copyright (C) 2016 Ordina
  */

package nl.ordina

import org.axonframework.commandhandling.TargetAggregateIdentifier

import scala.annotation.meta.{field, getter}

package object commands {

  case class CreateToDoItemCommand(@(TargetAggregateIdentifier@field) todoId: String, description: String)

  case class MarkCompletedCommand(@(TargetAggregateIdentifier@field) todoId: String)

}

package object events {

  case class ToDoItemCreatedEvent(todoId: String, description: String)

  case class ToDoItemCompletedEvent(todoId: String)

}
