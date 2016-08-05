package nl.ordina.personen.handlers.soap

import nl.ordina.personen.event
import nl.ordina.personen.event.{PersoonGeboren, PersoonOverleden}
import org.axonframework.eventhandling.{EventHandler, SimpleEventHandlerInvoker, SubscribingEventProcessor}

import scala.collection.mutable

class SoapEventHandler {

  val eventProcessor = new SubscribingEventProcessor(new SimpleEventHandlerInvoker("queue", this), event.eventStore)
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
