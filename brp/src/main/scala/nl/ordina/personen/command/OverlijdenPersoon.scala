package nl.ordina.personen.command

import nl.ordina.personen.datatype.Burgerservicenummer
import nl.ordina.personen.datatype.groep.Overlijden
import org.axonframework.commandhandling.TargetAggregateIdentifier

import scala.annotation.meta.field

case class OverlijdenPersoon(
  @(TargetAggregateIdentifier@field) burgerservicenummer: Burgerservicenummer,
  overlijden: Overlijden
)