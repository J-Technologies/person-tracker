/**
  * Copyright (C) 2016 Ordina
  */

package nl.ordina.personen.domein.entity

import nl.ordina.personen.datatype.SoortPersoon.INGESCHREVENE
import nl.ordina.personen.datatype.{Burgerservicenummer, SamengesteldeNaam, SoortPersoon}

abstract class Persoon(val soortPersoon: SoortPersoon = INGESCHREVENE)

case class NatuurlijkPersoon(
  override val soortPersoon: SoortPersoon,
  burgerservicenummer: Burgerservicenummer,
  samengesteldeNaam: SamengesteldeNaam
) extends Persoon(soortPersoon)
