package nl.ordina.personen.handlers.json

import nl.ordina.personen.datatype.BurgelijkeStaat
import nl.ordina.personen.event
import nl.ordina.personen.event.{HuwelijkGecreeërd, PersoonGeboren, PersoonOverleden}
import nl.ordina.personen.handlers.json
import org.axonframework.eventhandling.{EventHandler, SimpleEventHandlerInvoker, TrackingEventProcessor}

import scala.collection.JavaConversions._
import scala.collection.mutable

class JsonEventHandler {

  val eventProcessor = new TrackingEventProcessor("json",
    new SimpleEventHandlerInvoker(this),
    event.eventStore,
    json.tokenStore)
  val queue = new mutable.Queue[String]()

  def start(): Unit = eventProcessor.start()

  @EventHandler
  def handle(event: PersoonGeboren): Unit =
    persoonMapper.save(
      PersoonEntry(
        event.bsn.value,
        event.naam.toString,
        event.geslacht.naam,
        event.geboorte.datum.value.toString,
        event.ouders.map(bsn => bsn.value),
        BurgelijkeStaat.ONGEHUWD.naam,
        null,
        overleden = false
      )
    )

  @EventHandler
  def handle(event: PersoonOverleden): Unit =
    Option(persoonMapper.get(event.bsn.value)) match {
      case Some(persoon) => {
        {
          {
            persoon.overleden = true
            persoonMapper.save(persoon)
          }
        }
      }
      case None => throw new Error("person not found")
    }

  @EventHandler
  def handle(event: HuwelijkGecreeërd): Unit =
    Option(persoonMapper.get(event.bsn.value)) match {
      case Some(persoon) => {
        {
          {
            persoon.burgelijkeStaat = BurgelijkeStaat.GEHUWD.naam
            persoon.partner = event.partner.value
            persoonMapper.save(persoon)
          }
        }
      }
      case None => throw new Error("person not found")
    }
}
