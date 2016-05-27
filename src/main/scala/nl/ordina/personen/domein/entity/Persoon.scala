/**
  * Copyright (C) 2016 Ordina
  */

package nl.ordina.personen.domein.entity

import nl.ordina.personen.command.GeboorteInNederland
import nl.ordina.personen.datatype.Geslachtsaanduiding.ONBEKEND
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
  var geslacht: Geslachtsaanduiding = ONBEKEND
  var geboorte: Geboorte = null
  var bijhoudingsPartij: Partij = null

  @CommandHandler
  def this(command: GeboorteInNederland) = {
    this()
    apply(
      PersoonGeboren(
        command.burgerservicenummer,
        command.naam,
        command.geslacht,
        command.geboorte,
        command.bijhoudingspartij
      )
    )
  }

  @EventSourcingHandler
  def on(event: PersoonGeboren) = {
    this.burgerservicenummer = event.bsn
    this.samengesteldeNaam = event.naam
    this.geboorte = event.geboorte
    this.geslacht = event.geslacht
    this.bijhoudingsPartij = event.bijhoudingspartij

  }
}
