package nl.ordina.personen.datatypes

import org.scalatest.{FunSuite, Matchers}

class GeslachtsaanduidingTest extends FunSuite with Matchers {

  test("test VROUW") {
    VROUW.code shouldBe "V"
    VROUW.naam shouldBe "Vrouw"
  }

  test("test ONBEKEND") {
    ONBEKEND.code shouldBe "O"
    ONBEKEND.naam shouldBe "Onbekend"
  }

  test("test MAN") {
    MAN.code shouldBe "M"
    MAN.naam shouldBe "Man"
  }

}
