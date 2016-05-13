/**
  * Copyright (C) 2016 Ordina
  */

package nl.ordina.personen.domein.entity

import nl.ordina.personen.datatype.{Burgerservicenummer, INGESCHREVENE}
import org.scalatest.{FunSuite, Matchers}

/**
  * @author Eric Malotaux
  * @date 5/13/16.
  */
class NatuurlijkPersoonTest extends FunSuite with Matchers {
  val TEST_BSN: Int = 881011320
  val bsn: Burgerservicenummer = Burgerservicenummer(TEST_BSN)

  test("een geldig natuurlijk persoon") {
    val persoon: NatuurlijkPersoon = NatuurlijkPersoon(INGESCHREVENE, bsn)
    persoon.soortPersoon should be(INGESCHREVENE)
    persoon.burgerservicenummer should be(bsn)
  }

}
