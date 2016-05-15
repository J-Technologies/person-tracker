package nl.ordina.personen

import nl.ordina.personen.datatype.SamengesteldeNaam

package object command {

  case class GeboorteInNederland(
    naamEnNaamgebruik: SamengesteldeNaam
  )

}
