/**
  * Copyright (C) 2016 Ordina
  */

package nl.ordina.personen.datatype

import org.scalatest.{FunSuite, Matchers}

class GeslachtsaanduidingTest extends FunSuite with Matchers {

  test("test VROUW") {
    Geslachtsaanduiding.VROUW.code shouldBe "V"
    Geslachtsaanduiding.VROUW.naam shouldBe "Vrouw"
  }

  test("test ONBEKEND") {
    Geslachtsaanduiding.ONBEKEND.code shouldBe "O"
    Geslachtsaanduiding.ONBEKEND.naam shouldBe "Onbekend"
  }

  test("test MAN") {
    Geslachtsaanduiding.MAN.code shouldBe "M"
    Geslachtsaanduiding.MAN.naam shouldBe "Man"
  }

}
