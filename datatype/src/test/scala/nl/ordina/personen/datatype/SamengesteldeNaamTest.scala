/**
  * Copyright (C) 2016 Ordina
  */

package nl.ordina.personen.datatype

import org.scalatest.{FunSuite, Matchers}

class SamengesteldeNaamTest extends FunSuite with Matchers {

  test("met twee voornamen zonder voorvoegsel") {
    val naam = SamengesteldeNaam(
      Voornamen("Eric", "Jan"),
      Geslachtsnaam(Geslachtsnaamstam("Malotaux"))
    )
    naam.toString should be("Eric Jan Malotaux")
  }

  test("met twee voornamen en voorvoegsel") {
    val naam: SamengesteldeNaam = SamengesteldeNaam(
      Voornamen("Teije", "Sibrant"),
      Geslachtsnaam(Geslachtsnaamstam("Sloten"), Some(Voorvoegsel("van")))
    )
    naam.toString should be("Teije Sibrant van Sloten")
  }

  test("met een raar scheidingsteken") {
    val naam = SamengesteldeNaam(
      Voornamen("Pieter"),
      Geslachtsnaam(Geslachtsnaamstam("Hond"), Some(Voorvoegsel("d")), Scheidingsteken("'"))
    )
    naam.toString should be("Pieter d'Hond")
  }

  test("een jonkheer") {
    val naam = SamengesteldeNaam(
      Voornamen("Claus", "George", "Willem", "Otto", "Frederik", "Geert"),
      Geslachtsnaam(
        Geslachtsnaamstam("Amsberg"),
        Some(Voorvoegsel("van")),
        predicaat = Some(Predicaat.JONKHEER),
        titel = Some(AdellijkeTitel.PRINS)
      )
    )
    naam.toString should be("Jonkheer Claus George Willem Otto Frederik Geert Prins van Amsberg")
  }

}
