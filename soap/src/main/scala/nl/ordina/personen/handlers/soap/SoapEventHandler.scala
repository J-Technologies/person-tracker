package nl.ordina.personen.handlers.soap

import nl.ordina.personen.event
import nl.ordina.personen.event.{PersoonGeboren, PersoonOverleden}
import org.axonframework.eventhandling.{EventHandler, SimpleEventHandlerInvoker, TrackingEventProcessor}

import scala.collection.mutable

class SoapEventHandler {

  val eventProcessor = new TrackingEventProcessor("soap", new SimpleEventHandlerInvoker(this), event.eventStore, event.tokenStore)
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

object SoapEventHandler {
  def main(args: Array[String]) {
    new SoapEventHandler()
  }
}
