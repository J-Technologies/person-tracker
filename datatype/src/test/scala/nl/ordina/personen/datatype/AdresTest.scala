package nl.ordina.personen.datatype

import java.util.UUID

import org.scalatest.{FunSuite, Matchers}

class AdresTest extends FunSuite with Matchers {
  test("Gewone postcode") {
    val postcode: Postcode = Postcode("1234AB")
  }

  test("Postcode met te kort nummerdeel") {
    the[AssertionError] thrownBy Postcode("123AB") should have message "assertion failed: 123AB is geen geldige " +
      "postcode"
  }

  test("Postcode met te lang nummerdeel") {
    the[AssertionError] thrownBy Postcode("12345AB") should have message "assertion failed: 12345AB is geen geldige " +
      "postcode"
  }

  test("Postcode met letterdeel voor nummerdeel") {
    the[AssertionError] thrownBy Postcode("AB1234") should have message "assertion failed: AB1234 is geen geldige " +
      "postcode"
  }

  test("Postcode met teveel letters") {
    the[AssertionError] thrownBy Postcode("1234ABC") should have message "assertion failed: 1234ABC is geen geldige " +
      "postcode"
  }

  test("Postcode met SS als letterdeel") {
    the[AssertionError] thrownBy Postcode("1234SS") should have message "assertion failed: 1234SS is geen geldige " +
      "postcode"
  }

  test("Compleet geldig adres") {
    Adres(
      AdresIdentificatie(UUID.randomUUID().toString.substring(0, 16)),
      Gemeente("0034"),
      NaamOpenbareRuimte("Herfststraat"),
      Huisnummer(24), Postcode("1234AB")
    )
  }

  test("Compleet geldig adres met strings") {
    Adres(
      UUID.randomUUID().toString.substring(0, 16),
      "0034",
      "Herfststraat",
      24,
      "1234AB"
    )
  }

  test("Zoek een adres op") {
    var adres: Adres = Adres("3015ba-nieuwe-binnenweg-10-a")

    adres.id.value should be("3015ba-nieuwe-binnenweg-10-a")
    adres.gemeente.naam.value should be("Rotterdam")
    adres.huisnummer.value should be(10)
    adres.straatNaam.value should be("Nieuwe Binnenweg")
    adres.postcode.value should be("3015BA")

    adres = Adres("1051tt-joan-melchior-kemperstraat-62-3")

    adres.id.value should be("1051tt-joan-melchior-kemperstraat-62-3")
    adres.gemeente.naam.value should be("Amsterdam")
    adres.huisnummer.value should be(62)
    adres.straatNaam.value should be("Joan Melchior Kemperstraat")
    adres.postcode.value should be("1051TT")
  }
}
