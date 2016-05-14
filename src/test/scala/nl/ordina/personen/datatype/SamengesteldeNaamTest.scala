/**
  * Copyright (C) 2016 Ordina
  */

package nl.ordina.personen.datatype

import org.scalatest.{FunSuite, Matchers}

class SamengesteldeNaamTest extends FunSuite with Matchers {

  test("met twee voornamen zonder voorvoegsel") {
    val naam = SamengesteldeNaam(
      voornamen = Voornamen("Eric", "Jan"),
      geslachtsnaamstam = Geslachtsnaamstam("Malotaux")
    )
    naam.toString should be("Eric Jan Malotaux")
  }

  test("met twee voornamen en voorvoegsel") {
    val naam: SamengesteldeNaam = SamengesteldeNaam(
      voornamen = Voornamen("Teije", "Sibrant"),
      voorvoegsel = Some(Voorvoegsel("van")),
      geslachtsnaamstam = Geslachtsnaamstam("Sloten")
    )
    naam.toString should be("Teije Sibrant van Sloten")
  }

  test("met een raar scheidingsteken") {
    val naam = SamengesteldeNaam(
      voornamen = Voornamen("Pieter"),
      voorvoegsel = Some(Voorvoegsel("d")),
      scheidingsteken = Some(Scheidingsteken("'")),
      geslachtsnaamstam = Geslachtsnaamstam("Hond")
    )
    naam.toString should be("Pieter d'Hond")
  }

}
