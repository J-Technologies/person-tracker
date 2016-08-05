package nl.ordina.personen.datatype

import org.scalatest.{FunSuite, Matchers}

class VoornamenTest extends FunSuite with Matchers {

  test("te lange voornamen") {
    the[AssertionError] thrownBy Voornamen(
      "een", "twee", "drie", "vier", "vijf", "zes", "zeven", "acht", "negen",
      "tien", "elf", "twaalf", "dertien", "veertien", "vijftien", "zestien", "zeventien", "achtien", "negentien",
      "twintig", "eenentwintig", "tweeentwintig", "drieentwintig", "vierentwintig", "vijfentwintig", "zesentwintig",
      "zeventwintig", "achtentwintig"
    ) should have message
      "assertion failed: voornamen mogen samen niet langer dan 200 tekens zijn"
  }

}
