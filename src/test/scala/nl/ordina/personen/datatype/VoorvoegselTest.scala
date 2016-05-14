/**
  * Copyright (C) 2016 Ordina
  */

package nl.ordina.personen.datatype

import org.scalatest.{FunSuite, Matchers}

/**
  * @author Eric Malotaux
  * @date 5/14/16.
  */
class VoorvoegselTest extends FunSuite with Matchers {
  test("geldig voorvoegsel") {
    val voorvoegsel: Voorvoegsel = Voorvoegsel("van der")
    voorvoegsel.toString should be("van der")
  }

  test("te lang voorvoegsel") {
    the[AssertionError] thrownBy Voorvoegsel("te lang voorvoegsel") should have message
      "assertion failed: te lang voorvoegsel komt niet voor in de lijst van toegestane voorvoegsels"
  }

  test("onbekend voorvoegsel") {
    the[AssertionError] thrownBy Voorvoegsel("onbekend") should have message
      "assertion failed: onbekend komt niet voor in de lijst van toegestane voorvoegsels"
  }

  test("bekend voorvoegsel") {
    Voorvoegsel("over de").toString should be("over de")
  }

}
