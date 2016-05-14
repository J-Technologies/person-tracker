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
  object SoortPersoon {
    case object INGESCHREVENE extends SoortPersoon("I", "Ingeschrevene")
    case object NIET_INGESCHREVENE extends SoortPersoon("N", "Niet-ingeschrevene")
    case object GERELATEERDE extends SoortPersoon("G", "Gerelateerde")
  }

  sealed abstract class Geslachtsaanduiding(val code: String, val naam: String)
  object Geslachtsaanduiding {
    case object MAN extends Geslachtsaanduiding("M", "Man")
    case object VROUW extends Geslachtsaanduiding("V", "Vrouw")
    case object ONBEKEND extends Geslachtsaanduiding("O", "Onbekend")
  }

  abstract class SimpleValueObject[T](value: T) {
    override def toString = value.toString
  }

  abstract class StringMetBeperkteLengte(value: String, length: Int) extends SimpleValueObject(value) {
    assert(value.length <= length, s"lengte is ${value.length}, maar mag maximaal $length zijn")
  }

  abstract class Waardenlijst[T](value: T, lijst: Set[T]) extends SimpleValueObject {
    private val contains: Boolean = lijst(value)
    assert(contains, s"$value komt niet voor in de lijst van toegestane voorvoegsels")
  }
}
