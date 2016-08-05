package nl.ordina.personen.command

import nl.ordina.personen.datatype.Geslachtsaanduiding.MAN
import nl.ordina.personen.datatype._
import nl.ordina.personen.datatype.groep.Geboorte
import nl.ordina.personen.domein.entity.NatuurlijkPersoon
import nl.ordina.personen.event.PersoonGeboren
import org.axonframework.test.{FixtureConfiguration, Fixtures}
import org.scalatest.{FunSuite, Matchers}

class GeboorteInNederlandTest extends FunSuite with Matchers {
  val fixture: FixtureConfiguration[NatuurlijkPersoon] = Fixtures.newGivenWhenThenFixture(classOf[NatuurlijkPersoon])

  test("Creëer een 'GeboorteInNederland'") {
    val bsn: Burgerservicenummer = Burgerservicenummer.nieuw
    val geboorte = GeboorteInNederland(
      bsn,
      SamengesteldeNaam(Voornamen("Dirk"), Geslachtsnaam(Geslachtsnaamstam("Luijk"))),
      MAN,
      Geboorte(Datum("1993-01-01"), Gemeente("0505")),
      Partij("000505")
    )

    fixture.givenNoPriorActivity().when(geboorte).expectEvents(
      PersoonGeboren(bsn, geboorte.naam, geboorte.geslacht, geboorte.geboorte, geboorte.bijhoudingspartij)
    )
  }

}

