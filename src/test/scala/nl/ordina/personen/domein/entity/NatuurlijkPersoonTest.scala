package nl.ordina.personen.domein.entity

import nl.ordina.personen.domein.datatype.{Burgerservicenummer, INGESCHREVENE}
import org.scalatest.{FunSuite, Matchers}

/**
	* @author Eric Malotaux
	* @date 5/13/16.
	*/
class NatuurlijkPersoonTest extends FunSuite with Matchers {
	val bsn: Burgerservicenummer = Burgerservicenummer(881011320)

	test("een geldig natuurlijk persoon") {
		val persoon: NatuurlijkPersoon = NatuurlijkPersoon(INGESCHREVENE, bsn)
		persoon.soortPersoon should be(INGESCHREVENE)
		persoon.burgerservicenummer should be(bsn)
	}

}
