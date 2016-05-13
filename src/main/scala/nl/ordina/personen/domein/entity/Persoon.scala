/**
  * Copyright (C) 2016 Ordina
  */

package nl.ordina.personen.domein.entity

import nl.ordina.personen.domein.datatype.{Burgerservicenummer, INGESCHREVENE, SoortPersoon}

/**
  * @author Eric Malotaux
  * @date 5/13/16.
  */

abstract class Persoon(val soortPersoon: SoortPersoon = INGESCHREVENE)

class NatuurlijkPersoon(
  soortPersoon: SoortPersoon,
  val burgerservicenummer: Burgerservicenummer
) extends Persoon(soortPersoon)
object NatuurlijkPersoon {
  def apply(
    soortPersoon: SoortPersoon,
    burgerservicenummer: Burgerservicenummer
  ): NatuurlijkPersoon = new NatuurlijkPersoon(soortPersoon, burgerservicenummer)
}
