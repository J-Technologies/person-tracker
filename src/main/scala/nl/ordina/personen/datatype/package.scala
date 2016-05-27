/**
  * Copyright (C) 2016 Ordina
  */

package nl.ordina.personen

import java.time.format.DateTimeFormatter
import java.time.{Instant, LocalDate, ZoneId}

package object datatype {

  sealed abstract class JaOfNee
  object JaOfNee {
    object Ja extends JaOfNee
    object Nee extends JaOfNee
  }

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
    override def toString: String = value.toString
  }

  abstract class StringMetBeperkteLengte(value: String, length: Int) extends SimpleValueObject(value) {
    assert(value.length <= length, s"lengte is ${value.length}, maar mag maximaal $length zijn")
  }

  abstract class Waardenlijst[T](value: T, lijst: Set[T]) extends SimpleValueObject {
    private val contains: Boolean = lijst(value)
    assert(contains, s"$value komt niet voor in de lijst van toegestane voorvoegsels")
  }

  case class Datum(value: LocalDate) extends SimpleValueObject(value) with Ordered[Datum] {
    override def compare(that: Datum): Int = this.value.compareTo(that.value)
    def +(dagen: Int): Datum = Datum(value.plusDays(dagen))
    def -(dagen: Int): Datum = Datum(value.minusDays(dagen))
  }
  object Datum {
    def apply(value: Instant): Datum = apply(value.atZone(ZoneId.systemDefault).toLocalDate)
    def vandaag: Datum = apply(Instant.now)
    def morgen: Datum = vandaag + 1
    def gisteren: Datum = vandaag - 1
    def apply(value: String): Datum = {
      if (value.contains("-")) apply(LocalDate.parse(value))
      else apply(LocalDate.parse(value, DateTimeFormatter.BASIC_ISO_DATE))
    }
  }
}