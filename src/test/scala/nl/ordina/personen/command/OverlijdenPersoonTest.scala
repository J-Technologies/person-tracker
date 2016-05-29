package nl.ordina.personen.command

import nl.ordina.personen.datatype.Geslachtsaanduiding.MAN
import nl.ordina.personen.datatype._
import nl.ordina.personen.datatype.groep.{Geboorte, Overlijden}
import nl.ordina.personen.domein.entity.NatuurlijkPersoon
import nl.ordina.personen.event.{PersoonGeboren, PersoonOverleden}
import org.axonframework.test.Fixtures
import org.scalatest.{FunSuite, Matchers}

class OverlijdenPersoonTest extends FunSuite with Matchers {

  private var bsn: Burgerservicenummer = Burgerservicenummer.nieuw
  private val geboren: PersoonGeboren = PersoonGeboren(
    bsn,
    SamengesteldeNaam(voornamen = Voornamen("Dirk"), geslachtsnaamstam = Geslachtsnaamstam("Luijk")),
    MAN,
    Geboorte(Datum("1993-01-01"), Gemeente("0505")),
    Partij("000505")
  )
  private val overleden: PersoonOverleden = PersoonOverleden(bsn, Overlijden(Datum.gisteren, Gemeente("0505")))
  private var fixture = Fixtures.newGivenWhenThenFixture(classOf[NatuurlijkPersoon])

  test("een bestaande, levende persoon is overleden") {
    fixture
      .given(geboren)
      .when(OverlijdenPersoon(bsn, Overlijden(Datum.gisteren, Gemeente("0505"))))
      .expectEvents(overleden)
  }

  test("een reeds overleden persoon is overleden") {
    fixture
      .given(geboren, overleden)
      .when(OverlijdenPersoon(bsn, Overlijden(Datum.gisteren, Gemeente("0505"))))
      .expectException(classOf[IllegalStateException])
  }

}
