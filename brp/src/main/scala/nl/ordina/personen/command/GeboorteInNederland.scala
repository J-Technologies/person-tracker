package nl.ordina.personen.command

import nl.ordina.personen.datatype._
import nl.ordina.personen.datatype.groep.Geboorte
import org.axonframework.commandhandling.TargetAggregateIdentifier

case class GeboorteInNederland(
  @TargetAggregateIdentifier burgerservicenummer: Burgerservicenummer,
  naam: SamengesteldeNaam,
  geslacht: Geslachtsaanduiding = null,
  geboorte: Geboorte,
  ouders: List[Burgerservicenummer] = null,
  adres: Adres = null,
  bijhoudingspartij: Partij
) {
  assert(ouders.size >= 0 && ouders.size <= 2)
}
