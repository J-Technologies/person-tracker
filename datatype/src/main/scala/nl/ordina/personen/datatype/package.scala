/**
  * Copyright (C) 2016 Ordina
  */

package nl.ordina.personen

import java.time.format.DateTimeFormatter
import java.time.{Instant, LocalDate, ZoneId}

import scala.util.matching.Regex

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

    def fromString(geslacht: String): Geslachtsaanduiding = geslacht match {
      case "man" => MAN
      case "vrouw" => VROUW
      case "onbekend" => ONBEKEND
    }

    case object MAN extends Geslachtsaanduiding("M", "Man")
    case object VROUW extends Geslachtsaanduiding("V", "Vrouw")
    case object ONBEKEND extends Geslachtsaanduiding("O", "Onbekend")
  }

  abstract class SimpleValueObject[T](value: T) {
    override def toString: String = value.toString
  }

  abstract class StringMetVasteLengte(value: String, lengte: Int) extends SimpleValueObject(value) {
    assert(value.length == lengte, s"lengte van $value is ${value.length}, maar moet precies $lengte zijn")
  }

  abstract class StringMetBeperkteLengte(value: String, length: Int) extends SimpleValueObject(value) {
    assert(value.length <= length, s"lengte is ${value.length}, maar mag maximaal $length zijn")
  }

  abstract class NummerMetBeperkteLengte(value: Int, length: Int) extends SimpleValueObject(value) {
    private val valueAsString: String = value.toString
    assert(valueAsString.length <= length, s"lengte is ${valueAsString.length}, maar mag maximaal $length zijn")
  }

  abstract class StringMetRegex(value: String, regex: Regex) extends SimpleValueObject(value) {
    private val result: Boolean = value match {
      case regex(_*) => true
      case _ => false
    }
    assert(result, s"$value is geen geldige postcode")
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

  sealed abstract case class Bijhoudingsaard(code: String, omschrijving: String)
  object Bijhoudingsaard {
    object INGEZETENE extends Bijhoudingsaard("I", "Ingezetene")
    object NIET_INGEZETENE extends Bijhoudingsaard("N", "Niet-ingezetene")
    object ONBEKEND extends Bijhoudingsaard("?", "Onbekend")
  }

  sealed case class ErnstControleRegel(code: String, naam: String)
  object BLOKKEREND extends ErnstControleRegel("B", "Blokkerend")
  object DEBLOKKEERBAAR extends ErnstControleRegel("D", "Deblokkeerbaar")
  object WAARSCHUWING extends ErnstControleRegel("W", "Waarschuwing")

  sealed case class ControleRegel(code: String, omschrijving: String, ernst: ErnstControleRegel)
  object BRAL0012 extends ControleRegel("BRAL0012", "%s moet voldoen aan de elfproef", BLOKKEREND)

  case class ControleRegelException(regel: ControleRegel, argumenten: Any*) extends Exception(regel.omschrijving) {
    override def getMessage: String = {
      super.getMessage.format(argumenten: _*)
    }
  }

  def controle(assertion: Boolean, regel: ControleRegel, argumenten: Any*): Unit = {
    if (!assertion) throw new ControleRegelException(regel, argumenten: _*)
  }
}
