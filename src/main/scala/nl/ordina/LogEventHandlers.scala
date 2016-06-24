package nl.ordina

import nl.ordina.personen.event.{PersoonGeboren, PersoonOverleden}
import org.axonframework.eventhandling.annotation.EventHandler

class LogEventHandlers {

  @EventHandler
  def handle(event: PersoonGeboren) {
    println(s"PersoonGeboren: ${event.bsn} $event")
  }

  @EventHandler
  def handle(event: PersoonOverleden) {
    println(s"PersoonOverleden: ${event.bsn}")
  }
}
