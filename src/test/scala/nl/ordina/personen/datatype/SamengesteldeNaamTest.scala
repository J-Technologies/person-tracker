/**
  * Copyright (C) 2016 Ordina
  */

package nl.ordina.personen.datatype

import org.scalatest.{FunSuite, Matchers}

class SamengesteldeNaamTest extends FunSuite with Matchers {

  test("een gewone samengestelde naam") {
    val naam: SamengesteldeNaam = SamengesteldeNaam(
      Voornamen("Teije", "Sibrant"),
      Voorvoegsel("van"),
      Geslachtsnaam("Sloten")
    )
    naam.toString should be("Teije Sibrant van Sloten")
  }

}
