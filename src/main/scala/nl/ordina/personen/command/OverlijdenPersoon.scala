package nl.ordina.personen.command

import nl.ordina.personen.datatype.Burgerservicenummer
import nl.ordina.personen.datatype.groep.Overlijden
import org.axonframework.scynapse.annotations.aggregateId

case class OverlijdenPersoon(
  @aggregateId burgerservicenummer: Burgerservicenummer,
  overlijden: Overlijden
)