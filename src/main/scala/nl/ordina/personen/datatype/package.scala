/**
  * Copyright (C) 2016 Ordina
  */

package nl.ordina.personen

/**
  * @author Eric Malotaux
  * @date 5/13/16.
  */
package object datatype {

  sealed abstract class SoortPersoon(val code: String, val naam: String)
  case object INGESCHREVENE extends SoortPersoon("I", "Ingeschrevene")
  case object NIET_INGESCHREVENE extends SoortPersoon("N", "Niet-ingeschrevene")
  case object GERELATEERDE extends SoortPersoon("G", "Gerelateerde")

  sealed abstract class Geslachtsaanduiding(val code: String, val naam: String)
  case object MAN extends Geslachtsaanduiding("M", "Man")
  case object VROUW extends Geslachtsaanduiding("V", "Vrouw")
  case object ONBEKEND extends Geslachtsaanduiding("O", "Onbekend")

  abstract class SimpleValueObject[T](value: T) {
    override def toString = value.toString
  }
  case class Voornamen(values: String*) {
    override def toString = values.mkString(" ")
  }

  case class SamengesteldeNaam(
    voornamen: Voornamen,
    voorvoegsel: Voorvoegsel,
    geslachtsnaam: Geslachtsnaam
  ) {
    override def toString = s"$voornamen $voorvoegsel $geslachtsnaam"
  }

  case class Voorvoegsel(value: String) extends SimpleValueObject(value)
  case class Geslachtsnaam(value: String) extends SimpleValueObject(value)

}
