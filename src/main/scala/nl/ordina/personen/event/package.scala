package nl.ordina.personen

import nl.ordina.personen.datatype._

package object event {

  case class PersoonGeboren(
    bsn: Burgerservicenummer,
    naam: SamengesteldeNaam,
    geslacht: Geslachtsaanduiding,
    geboorte: Geboorte,
    bijhoudingspartij: Partij
  )
}
