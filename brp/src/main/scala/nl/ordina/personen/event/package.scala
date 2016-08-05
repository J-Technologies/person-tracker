package nl.ordina.personen

import nl.ordina.personen.datatype._
import nl.ordina.personen.datatype.groep.{Geboorte, Overlijden}

package object event {

  case class PersoonGeboren(
    bsn: Burgerservicenummer,
    naam: SamengesteldeNaam,
    geslacht: Geslachtsaanduiding,
    geboorte: Geboorte,
    bijhoudingspartij: Partij
  )

  case class PersoonOverleden(
    bsn: Burgerservicenummer,
    overlijden: Overlijden
  )
}
