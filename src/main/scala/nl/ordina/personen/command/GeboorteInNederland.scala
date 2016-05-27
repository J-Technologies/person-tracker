package nl.ordina.personen.command

import nl.ordina.personen.datatype.{Datum, Gemeente, Partij, SamengesteldeNaam}

case class GeboorteInNederland(
  naamEnNaamgebruik: SamengesteldeNaam,
  datumGeboorte: Datum,
  gemeenteGeboorte: Gemeente,
  bijhoudingspartij: Partij
)
