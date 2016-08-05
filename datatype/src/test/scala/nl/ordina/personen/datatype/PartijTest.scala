package nl.ordina.personen.datatype

import nl.ordina.personen.datatype.SoortPartij.GEMEENTE
import org.scalatest.{FunSuite, Matchers}

class PartijTest extends FunSuite with Matchers {

  test("Gemeente Dordrecht") {
    val partij = Partij("000505")

    partij.code.value should be("000505")
    partij.naam.value should be("Gemeente Dordrecht")
    partij.soort should be (GEMEENTE)
  }
}
