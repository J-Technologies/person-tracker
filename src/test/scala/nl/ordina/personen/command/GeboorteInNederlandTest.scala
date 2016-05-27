package nl.ordina.personen.command

import nl.ordina.personen.datatype.Geslachtsaanduiding.MAN
import nl.ordina.personen.datatype._
import org.scalatest.{FunSuite, Matchers}

class GeboorteInNederlandTest extends FunSuite with Matchers {

  test("Creëer een 'GeboorteInNederland'") {
    val geboorte = GeboorteInNederland(
      SamengesteldeNaam(voornamen = Voornamen("Dirk"), geslachtsnaamstam = Geslachtsnaamstam("Luijk")),
      MAN,
      geboorte = Geboorte(Datum("1993-01-01"), Gemeente("0505")), bijhoudingspartij = Partij("000505")
    )
    geboorte.naamEnNaamgebruik.voornamen.toString should be("Dirk")
    geboorte.naamEnNaamgebruik.achternaam should be("Luijk")
    geboorte.geslacht should be(MAN)
    geboorte.bijhoudingspartij.naam should be(Partijnaam("Gemeente Dordrecht"))
    geboorte.geboorte.gemeente.naam.value should be("Dordrecht")
    geboorte.geboorte.datum.toString should be("1993-01-01")
  }

}
