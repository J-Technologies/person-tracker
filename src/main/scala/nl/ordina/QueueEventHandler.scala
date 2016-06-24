/**
  * Copyright (C) 2016 Ordina
  */

package nl.ordina

import nl.ordina.personen.event.{PersoonGeboren, PersoonOverleden}
import org.axonframework.eventhandling.EventHandler

import scala.collection.mutable

class QueueEventHandler {

  val queue = new mutable.Queue[String]()

  @EventHandler
  def handle(event: PersoonGeboren) {
    queue += s"PersoonGeboren: ${event.bsn} $event"
  }

  @EventHandler
  def handle(event: PersoonOverleden) {
    queue += s"PersoonOverleden: ${event.bsn}"
  }
}
