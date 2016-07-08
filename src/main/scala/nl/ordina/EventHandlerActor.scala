/**
  * Copyright (C) 2016 Ordina
  */

package nl.ordina

import akka.actor.Props
import akka.http.scaladsl.model.ws.{Message, TextMessage}
import akka.stream.actor.ActorPublisher
import nl.ordina.personen.event.{PersoonGeboren, PersoonOverleden}
import org.axonframework.eventhandling.{EventHandler, SimpleEventHandlerInvoker, SubscribingEventProcessor}
import org.axonframework.eventsourcing.eventstore.EventStore

class EventHandlerActor(eventStore: EventStore) extends ActorPublisher[Message] {

  val eventProcessor = new SubscribingEventProcessor(new SimpleEventHandlerInvoker("eventActor", this), eventStore)

  @scala.throws[Exception](classOf[Exception])
  override def preStart(): Unit = eventProcessor.start()

  @scala.throws[Exception](classOf[Exception])
  override def postStop(): Unit = eventProcessor.shutDown()

  override def receive: Receive = {
    case _ => println
  }

  @EventHandler
  def handle(event: PersoonGeboren): Unit = {
    if(isActive && totalDemand > 0) onNext(TextMessage(event.toString))
  }

  @EventHandler
  def handle(event: PersoonOverleden) {
    if(isActive && totalDemand > 0) onNext(TextMessage(event.toString))
  }

}

object EventHandlerActor {
  def props(eventStore: EventStore): Props = Props(new EventHandlerActor(eventStore))
}
