package nl.ordina.personen

import nl.ordina.personen.datatype.SamengesteldeNaam

/**
  * Copyright (C) 2016 Ordina
  */

/**
  * @author Eric Malotaux
  * @date 5/13/16.
  */
package object command {

  case class GeboorteInNederland(
    val naamEnNaamgebruik: SamengesteldeNaam
  )

}
