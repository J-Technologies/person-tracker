package nl.ordina.personen

import nl.ordina.personen.datatype.{Datum, Gemeentecode, Geslachtsaanduiding, SamengesteldeNaam}

package object command {

  case class GeboorteInNederland(
    naamEnNaamgebruik: SamengesteldeNaam,
    geslacht: Geslachtsaanduiding,
    datumGeboorte: Datum,
    gemeenteGeboorte: Gemeentecode
  )

}
