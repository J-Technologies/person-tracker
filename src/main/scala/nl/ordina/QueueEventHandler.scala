/**
  * Copyright (C) 2016 Ordina
  */

package nl.ordina

import nl.ordina.personen.event.{PersoonGeboren, PersoonOverleden}
import org.axonframework.eventhandling.{EventHandler, SimpleEventHandlerInvoker, SubscribingEventProcessor}
import org.axonframework.eventsourcing.eventstore.EventStore

import scala.collection.mutable

class QueueEventHandler(eventStore: EventStore) {

  val eventProcessor = new SubscribingEventProcessor(new SimpleEventHandlerInvoker("queue", this), eventStore)
  val queue = new mutable.Queue[String]()

  eventProcessor.start()

  @EventHandler
  def handle(event: PersoonGeboren) {
    queue += s"PersoonGeboren: ${event.bsn} $event"
  }

  @EventHandler
  def handle(event: PersoonOverleden) {
    queue += s"PersoonOverleden: ${event.bsn}"
  }
}
