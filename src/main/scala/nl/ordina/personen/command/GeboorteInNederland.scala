package nl.ordina.personen.command

import nl.ordina.personen.datatype._
import nl.ordina.personen.datatype.groep.Geboorte

case class GeboorteInNederland(
  burgerservicenummer: Burgerservicenummer,
  naam: SamengesteldeNaam,
  geslacht: Geslachtsaanduiding = null,
  geboorte: Geboorte,
  bijhoudingspartij: Partij
)
