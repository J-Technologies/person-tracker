package nl.ordina.personen.command

import nl.ordina.personen.datatype._

case class GeboorteInNederland(
  burgerservicenummer: Burgerservicenummer,
  naam: SamengesteldeNaam,
  geslacht: Geslachtsaanduiding = null,
  geboorte: Geboorte,
  bijhoudingspartij: Partij
)
