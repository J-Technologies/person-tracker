/**
  * Copyright (C) 2016 Ordina
  */

package nl.ordina.example

import java.util.UUID
import java.util.concurrent.Executors

import nl.ordina.commands.CreateToDoItemCommand
import org.axonframework.commandhandling.gateway.DefaultCommandGateway
import org.axonframework.commandhandling.{AggregateAnnotationCommandHandler, SimpleCommandBus}
import org.axonframework.eventhandling.{AnnotationEventListenerAdapter, SimpleEventHandlerInvoker, SubscribingEventProcessor}
import org.axonframework.eventsourcing.EventSourcingRepository
import org.axonframework.eventsourcing.eventstore.EmbeddedEventStore
import org.axonframework.eventsourcing.eventstore.inmemory.InMemoryEventStorageEngine
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
  val eventStore = new EmbeddedEventStore(new InMemoryEventStorageEngine)
  val repo = new EventSourcingRepository[ToDoItem](classOf[ToDoItem], eventStore)

  val testHandler: ToDoEventHandler = new ToDoEventHandler()

  def setupAxon() = {
    new SubscribingEventProcessor(new SimpleEventHandlerInvoker("test", testHandler), eventStore).start()
    new AggregateAnnotationCommandHandler(classOf[ToDoItem], repo).subscribe(commandBus)
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
    if (testHandler.queue.nonEmpty) {
      Text("Dequeued message -> " + testHandler.queue.dequeue())
    } else Text("Dequeued message -> nothing new")
  })

  def main(args: Array[String]) {
    val commandHandlerRegistration = setupAxon()
    setupBlazeServer()
    commandHandlerRegistration.close()
  }
}
