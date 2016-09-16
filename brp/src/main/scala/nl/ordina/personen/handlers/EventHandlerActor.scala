/**
  * Copyright (C) 2016 Ordina
  */

package nl.ordina.personen.handlers

import akka.actor.Props
import akka.http.scaladsl.model.ws.{Message, TextMessage}
import akka.stream.actor.ActorPublisher
import nl.ordina.personen.event
import nl.ordina.personen.event.{PersoonGeboren, PersoonOverleden}
import org.axonframework.eventhandling.{EventHandler, SimpleEventHandlerInvoker, SubscribingEventProcessor}

class EventHandlerActor extends ActorPublisher[Message] {

  val eventProcessor = new SubscribingEventProcessor("eventActor",
    new SimpleEventHandlerInvoker(this),
    event.eventStore)

  @scala.throws[Exception](classOf[Exception])
  override def preStart(): Unit = eventProcessor.start()

  @scala.throws[Exception](classOf[Exception])
  override def postStop(): Unit = eventProcessor.shutDown()

  override def receive: Receive = {
    case _ => println
  }

  @EventHandler
  def handle(event: PersoonGeboren): Unit = {
    if (isActive && totalDemand > 0) onNext(TextMessage(event.toString))
  }

  @EventHandler
  def handle(event: PersoonOverleden) {
    if (isActive && totalDemand > 0) onNext(TextMessage(event.toString))
  }

}

object EventHandlerActor {
  def props: Props = Props(new EventHandlerActor)
}
