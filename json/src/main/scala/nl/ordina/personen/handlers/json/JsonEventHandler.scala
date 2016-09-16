package nl.ordina.personen.handlers.json

import nl.ordina.personen.event
import nl.ordina.personen.event.{PersoonGeboren, PersoonOverleden}
import nl.ordina.personen.handlers.json
import org.axonframework.eventhandling.{EventHandler, SimpleEventHandlerInvoker, TrackingEventProcessor}

import scala.collection.mutable

class JsonEventHandler {

  val eventProcessor = new TrackingEventProcessor("json", new SimpleEventHandlerInvoker(this), event.eventStore, json.tokenStore)
  val queue = new mutable.Queue[String]()

  def start(): Unit = eventProcessor.start()

  @EventHandler
  def handle(event: PersoonGeboren): Unit =
    persoonMapper.save(PersoonEntry(event.bsn.value, event.naam.toString, overleden = false))

  @EventHandler
  def handle(event: PersoonOverleden): Unit =
    Option(persoonMapper.get(event.bsn.value)) match {
      case Some(persoon) => persoonMapper.save(PersoonEntry(persoon.bsn, persoon.naam, overleden = true))
      case None => throw new Error("person not found")
    }
}
