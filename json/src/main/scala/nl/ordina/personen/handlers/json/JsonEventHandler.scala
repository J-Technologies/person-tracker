package nl.ordina.personen.handlers.json

import nl.ordina.personen.event
import nl.ordina.personen.event.{PersoonGeboren, PersoonOverleden}
import nl.ordina.personen.handlers.json
import org.axonframework.eventhandling.{EventHandler, SimpleEventHandlerInvoker, TrackingEventProcessor}

import scala.collection.mutable

class JsonEventHandler(jsonRepository: JsonRepository) {

  val eventProcessor = new TrackingEventProcessor("json", new SimpleEventHandlerInvoker(this), event.eventStore, json.tokenStore)
  val queue = new mutable.Queue[String]()

  eventProcessor.start()

  @EventHandler
  def handle(event: PersoonGeboren): Unit = {
    jsonRepository.store(PersoonEntry(event.bsn.value, isOverleden = false))
  }

  @EventHandler
  def handle(event: PersoonOverleden) {
    jsonRepository.store(PersoonEntry(event.bsn.value, isOverleden = true))
  }
}

object JsonEventHandler {
  def apply(jsonRepository: JsonRepository): JsonEventHandler = new JsonEventHandler(jsonRepository)
}