/**
  * Copyright (C) 2016 Ordina
  */

package nl.ordina.personen.domein.entity

import nl.ordina.personen.datatype.Datum.vandaag
import nl.ordina.personen.datatype.Geslachtsaanduiding.MAN
import nl.ordina.personen.datatype._
import org.scalatest.{FunSuite, Matchers}

class NatuurlijkPersoonTest extends FunSuite with Matchers {
  val TEST_BSN: Int = 881011320
  val bsn: Burgerservicenummer = Burgerservicenummer(TEST_BSN)

  test("een geldig natuurlijk persoon") {
    val persoon: NatuurlijkPersoon = NatuurlijkPersoon(
      SoortPersoon.INGESCHREVENE,
      bsn,
      SamengesteldeNaam(voornamen = Voornamen("Eric", "Jan"), geslachtsnaamstam = Geslachtsnaamstam("Malotaux")),
      MAN,
      Geboorte(Datum("1955-10-17"), Gemeente("0518"))
    )
    persoon.soortPersoon should be(SoortPersoon.INGESCHREVENE)
    persoon.burgerservicenummer should be(bsn)
    persoon.samengesteldeNaam.toString should be("Eric Jan Malotaux")
    persoon.geslacht should be(MAN)
    persoon.geboorte.datum.toString should be("1955-10-17")
    persoon.geboorte.datum should be < vandaag
  }

}
