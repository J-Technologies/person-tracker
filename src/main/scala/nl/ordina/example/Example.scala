/**
  * Copyright (C) 2016 Ordina
  */

package nl.ordina.example

import java.util.UUID
import java.util.concurrent.Executors

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
import org.http4s.server.websocket.WS
import org.http4s.websocket.WebsocketBits._

import scala.concurrent.duration._
import scalaz.concurrent.Task
import scalaz.stream.{Exchange, Process, time}

object Example {

  private implicit val scheduledEC = Executors.newScheduledThreadPool(4)

  val commandBus = new SimpleCommandBus()
  val commandGateway = new DefaultCommandGateway(commandBus)
  val eventBus = new SimpleEventBus()
  val repo = new EventSourcingRepository[ToDoItem](classOf[ToDoItem], new CouchbaseEventStore)

  val testHandler: ToDoEventHandler = new ToDoEventHandler()

  def setupAxon() = {
    repo.setEventBus(eventBus)
    AggregateAnnotationCommandHandler.subscribe(classOf[ToDoItem], repo, commandBus)
    AnnotationEventListenerAdapter.subscribe(testHandler, eventBus)
  }

  def setupBlazeServer() = {
    BlazeBuilder.bindHttp(8123)
      .mountService(
        HttpService {
          case GET -> Root / "websocket" => WS(Exchange(somePolling, Process.halt))
          case GET -> Root / description =>
            commandGateway.send(new CreateToDoItemCommand(UUID.randomUUID().toString, description))
            Ok(description)
        }, "/todos"
      )
      .run
      .awaitShutdown()
  }

  val somePolling: Process[Task, Text] = time.awakeEvery(1 seconds).map(d => {
    if(testHandler.queue.nonEmpty) {
      Text("Dequeued message -> " + testHandler.queue.dequeue())
    } else Text("Dequeued message -> nothing new")
  })

  def main(args: Array[String]) {
    setupAxon()
    setupBlazeServer()
  }
}
