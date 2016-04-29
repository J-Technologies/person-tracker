package nl.ordina.personen.datatypes

import nl.ordina.personen.datatypes.Geslachtsaanduiding.GeslachtsaanduidingWaarde
import org.scalatest.{FunSuite, Matchers}

/**
  * @author Eric Malotaux
  * @date 4/29/16.
  */
class Geslachtsaanduiding$Test extends FunSuite with Matchers {

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
