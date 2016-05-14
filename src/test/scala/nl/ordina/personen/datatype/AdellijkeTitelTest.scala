package nl.ordina.personen.datatype

import nl.ordina.personen.datatype.AdellijkeTitel.{BARON, HERTOGIN}
import org.scalatest.{FunSuite, Matchers}

class AdellijkeTitelTest extends FunSuite with Matchers {

  test("baron") {
    val baron: AdellijkeTitel = AdellijkeTitel("B")
    baron should be(BARON)
    baron.toString should be("Baron")
  }

  test("hertogin") {
    val hertogin: AdellijkeTitel = AdellijkeTitel("HI")
    hertogin should be(HERTOGIN)
    hertogin.toString should be("Hertogin")
  }
}
