package nl.ordina.personen.datatype

import nl.ordina.personen.datatype.Datum.{gisteren, morgen, vandaag}
import org.scalatest.{FunSuite, Matchers}

class DatumTest extends FunSuite with Matchers {
  test("gisteren komt voor vandaag") {
    gisteren should be < vandaag
  }

  test("vandaag komt voor morgen") {
    vandaag should be < morgen
  }

  test("gisteren komt voor morgen") {
    gisteren should be < morgen
  }

  test("morgen min een is vandaag") {
    morgen - 1 should be(vandaag)
  }

  test("gisteren plus twee is morgen") {
    gisteren + 2 should be(morgen)
  }

}
