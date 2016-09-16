/**
  * Copyright (C) 2016 Ordina
  */

package nl.ordina.personen.domein.entity

import nl.ordina.personen.command.{GeboorteInNederland, Huwelijk, OverlijdenPersoon}
import nl.ordina.personen.datatype._
import nl.ordina.personen.datatype.groep.{Geboorte, Overlijden}
import nl.ordina.personen.event.{HuwelijkGecreeërd, PersoonGeboren, PersoonOverleden}
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.commandhandling.model.{AggregateIdentifier, AggregateLifecycle, AggregateRoot}
import org.axonframework.eventsourcing.EventSourcingHandler

@AggregateRoot
abstract class Persoon(val soortPersoon: SoortPersoon)

class NatuurlijkPersoon extends Persoon(SoortPersoon.INGESCHREVENE) {
  @AggregateIdentifier
  var burgerservicenummer: Burgerservicenummer = null
  var samengesteldeNaam: SamengesteldeNaam = null
  var geslacht: Geslachtsaanduiding = Geslachtsaanduiding.ONBEKEND
  var geboorte: Geboorte = null
  var overlijden: Overlijden = null
  var bijhoudingsPartij: Partij = null
  var bijhoudingsaard: Bijhoudingsaard = null

  var burgelijkeStaat: BurgelijkeStaat = BurgelijkeStaat.ONBEKEND
  var partner: Burgerservicenummer = null
  var huwelijksDatum: Datum = null
  var huwelijksGemeente: Gemeente = null

  @CommandHandler
  def this(command: GeboorteInNederland) = {
    this()
    if (burgerservicenummer != null) {
      throw new IllegalStateException(
        s"Persoon met burgerservicenummer ${command.burgerservicenummer} is reeds geboren"
      )
    }
    AggregateLifecycle.apply(
      PersoonGeboren(
        command.burgerservicenummer,
        command.naam,
        command.geslacht,
        command.geboorte,
        command.ouders,
        command.adres,
        command.bijhoudingspartij
      )
    )
  }

  @CommandHandler
  def handle(command: OverlijdenPersoon) = {
    if (overlijden != null) {
      throw new IllegalStateException(
        s"Persoon met burgerservicenummer ${command.burgerservicenummer} is reeds overleden"
      )
    }
    AggregateLifecycle.apply(PersoonOverleden(command.burgerservicenummer, command.overlijden))
  }

  @CommandHandler
  def handle(command: Huwelijk) = {
    if (burgelijkeStaat == BurgelijkeStaat.GEHUWD) {
      throw new IllegalStateException(
        s"Persoon met burgerservicenummer ${this.burgerservicenummer} is reeds getrouwd"
      )
    }
    AggregateLifecycle.apply(
      HuwelijkGecreeërd(
        command.burgerservicenummer,
        command.partner,
        command.datum,
        command.gemeente
      )
    )
  }

  @EventSourcingHandler
  def on(event: PersoonGeboren) = {
    this.burgerservicenummer = event.bsn
    this.samengesteldeNaam = event.naam
    this.geslacht = event.geslacht
    this.geboorte = event.geboorte
    this.bijhoudingsPartij = event.bijhoudingspartij
    this.bijhoudingsaard = Bijhoudingsaard.INGEZETENE
    this.burgelijkeStaat = BurgelijkeStaat.ONGEHUWD
  }

  @EventSourcingHandler
  def on(event: PersoonOverleden) = this.overlijden = event.overlijden

  @EventSourcingHandler
  def on(event: HuwelijkGecreeërd) = {
    this.burgelijkeStaat = BurgelijkeStaat.GEHUWD
    this.partner = event.partner
    this.huwelijksDatum = event.datum
    this.huwelijksGemeente = event.gemeente
  }
}
