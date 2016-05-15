package nl.ordina.personen.datatype

import org.scalatest.{FunSuite, Matchers}

class GeboorteTest extends FunSuite with Matchers {

  test("vandaag") {
    val geboorte: Geboorte = Geboorte(Datum.vandaag, Gemeente("0036"))
    geboorte.datum should be(Datum.vandaag)
  }

}
