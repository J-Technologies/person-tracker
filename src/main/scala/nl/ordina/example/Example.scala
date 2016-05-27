/**
  * Copyright (C) 2016 Ordina
  */

package nl.ordina.example

import java.util.UUID

import nl.ordina.commands.CreateToDoItemCommand
import nl.ordina.eventstore.CouchbaseEventStore
import org.axonframework.commandhandling.SimpleCommandBus
import org.axonframework.commandhandling.annotation.AggregateAnnotationCommandHandler
import org.axonframework.commandhandling.gateway.DefaultCommandGateway
import org.axonframework.eventhandling.SimpleEventBus
import org.axonframework.eventhandling.annotation.AnnotationEventListenerAdapter
import org.axonframework.eventsourcing.EventSourcingRepository
import org.http4s.HttpService
import org.http4s.dsl._
import org.http4s.server.blaze.BlazeBuilder

object Example {

  val commandBus = new SimpleCommandBus()
  val commandGateway = new DefaultCommandGateway(commandBus)
  val eventBus = new SimpleEventBus()
  val repo = new EventSourcingRepository[ToDoItem](classOf[ToDoItem], new CouchbaseEventStore)

  def setupAxon() = {
    repo.setEventBus(eventBus)
    AggregateAnnotationCommandHandler.subscribe(classOf[ToDoItem], repo, commandBus)
    AnnotationEventListenerAdapter.subscribe(new ToDoEventHandler(), eventBus)
  }

  def setupBlazeServer() = {
    BlazeBuilder.bindHttp(8123)
      .mountService(
        HttpService { case GET -> Root / description =>
          commandGateway.send(new CreateToDoItemCommand(UUID.randomUUID().toString, description))
          Ok(description)
        }, "/todos"
      )
      .run
      .awaitShutdown()
  }

  def main(args: Array[String]) {
    setupAxon()
    setupBlazeServer()
  }
}
