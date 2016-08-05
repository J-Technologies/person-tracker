package nl.ordina.personen.handlers.json

import nl.ordina.personen.event
import nl.ordina.personen.event.{PersoonGeboren, PersoonOverleden}
import org.axonframework.eventhandling.{EventHandler, SimpleEventHandlerInvoker, TrackingEventProcessor}

import scala.collection.mutable

class JsonEventHandler {

  val eventProcessor = new TrackingEventProcessor("json", new SimpleEventHandlerInvoker(this), event.eventStore, event.tokenStore)
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

object JsonEventHandler {

  def main(args: Array[String]) {
    new JsonEventHandler()
  }
}