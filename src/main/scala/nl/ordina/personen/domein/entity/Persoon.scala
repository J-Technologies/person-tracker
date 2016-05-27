/**
  * Copyright (C) 2016 Ordina
  */

package nl.ordina.personen.domein.entity

import nl.ordina.personen.command.GeboorteInNederland
import nl.ordina.personen.datatype.SoortPersoon.INGESCHREVENE
import nl.ordina.personen.datatype._
import nl.ordina.personen.event.PersoonGeboren
import org.axonframework.commandhandling.annotation.CommandHandler
import org.axonframework.eventsourcing.annotation.{AbstractAnnotatedAggregateRoot, AggregateIdentifier,
EventSourcingHandler}

abstract class Persoon(val soortPersoon: SoortPersoon = INGESCHREVENE) extends
  AbstractAnnotatedAggregateRoot[Burgerservicenummer]

class NatuurlijkPersoon extends Persoon {
  @AggregateIdentifier
  var burgerservicenummer: Burgerservicenummer = null
  var samengesteldeNaam: SamengesteldeNaam = null
  var geslacht: Geslachtsaanduiding = null
  var geboorte: Geboorte = null

  @CommandHandler
  def this(command: GeboorteInNederland) = {
    this()
    apply(PersoonGeboren)
  }

  @EventSourcingHandler
  def on(geboorte: PersoonGeboren) = ???
}
