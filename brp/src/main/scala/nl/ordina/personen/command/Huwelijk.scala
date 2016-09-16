package nl.ordina.personen.command

import nl.ordina.personen.datatype.{Burgerservicenummer, Datum, Gemeente}
import org.axonframework.commandhandling.TargetAggregateIdentifier

import scala.annotation.meta.field

case class Huwelijk(
  @(TargetAggregateIdentifier@field) burgerservicenummer: Burgerservicenummer,
  partner: Burgerservicenummer,
  datum: Datum,
  gemeente: Gemeente
)