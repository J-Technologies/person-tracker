package nl.ordina.personen.datatypes

import nl.ordina.personen.datatypes.Geslachtsaanduiding.GeslachtsaanduidingWaarde
import org.scalatest.{FunSuite, Matchers}

class GeslachtsaanduidingTest extends FunSuite with Matchers {

  test("test VROUW") {
    val vrouw: GeslachtsaanduidingWaarde = Geslachtsaanduiding.VROUW
    vrouw.code shouldBe "V"
    vrouw.naam shouldBe "Vrouw"
  }

  test("test ONBEKEND") {
    val onbekend: GeslachtsaanduidingWaarde = Geslachtsaanduiding.ONBEKEND
    onbekend.code shouldBe "O"
    onbekend.naam shouldBe "Onbekend"
  }

  test("test MAN") {
    val man: GeslachtsaanduidingWaarde = Geslachtsaanduiding.MAN
    man.code shouldBe "M"
    man.naam shouldBe "Man"
  }

}
