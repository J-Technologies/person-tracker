/**
  * Copyright (C) 2016 Ordina
  */

package nl.ordina.personen.domein.entity

import nl.ordina.personen.command.{GeboorteInNederland, OverlijdenPersoon}
import nl.ordina.personen.datatype.Bijhoudingsaard.INGEZETENE
import nl.ordina.personen.datatype.Geslachtsaanduiding.ONBEKEND
import nl.ordina.personen.datatype.SoortPersoon.INGESCHREVENE
import nl.ordina.personen.datatype._
import nl.ordina.personen.datatype.groep.{Geboorte, Overlijden}
import nl.ordina.personen.event.{PersoonGeboren, PersoonOverleden}
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.commandhandling.model.{AggregateIdentifier, AggregateLifecycle, AggregateRoot}
import org.axonframework.eventsourcing.EventSourcingHandler

@AggregateRoot
abstract class Persoon(val soortPersoon: SoortPersoon)

class NatuurlijkPersoon extends Persoon(INGESCHREVENE) {
  @AggregateIdentifier
  var burgerservicenummer: Burgerservicenummer = null
  var samengesteldeNaam: SamengesteldeNaam = null
  var geslacht: Geslachtsaanduiding = ONBEKEND
  var geboorte: Geboorte = null
  var overlijden: Overlijden = null
  var bijhoudingsPartij: Partij = null
  var bijhoudingsaard: Bijhoudingsaard = null

  @CommandHandler
  def this(command: GeboorteInNederland) = {
    this()
    AggregateLifecycle.apply(
      PersoonGeboren(
        command.burgerservicenummer,
        command.naam,
        command.geslacht,
        command.geboorte,
        command.bijhoudingspartij
      )
    )
  }

  @CommandHandler
  def handle(command: OverlijdenPersoon) = {
    if (overlijden != null)
      throw new IllegalStateException(
        s"Persoon met burgerservicenummer ${command.burgerservicenummer} is reeds overleden"
      )
    AggregateLifecycle.apply(PersoonOverleden(command.burgerservicenummer, command.overlijden))
  }

  @EventSourcingHandler
  def on(event: PersoonGeboren) = {
    this.burgerservicenummer = event.bsn
    this.samengesteldeNaam = event.naam
    this.geboorte = event.geboorte
    this.geslacht = event.geslacht
    this.bijhoudingsPartij = event.bijhoudingspartij
    this.bijhoudingsaard = INGEZETENE
  }

  @EventSourcingHandler
  def on(event: PersoonOverleden) = {
    this.overlijden = event.overlijden
  }
}
