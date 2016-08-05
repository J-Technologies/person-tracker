package nl.ordina.personen.datatype

import org.scalatest.{FunSuite, Matchers}

class GemeenteTest extends FunSuite with Matchers {

  test("Gemeente Almere") {
    val almere = Gemeente("0034")

    almere.code.value should be("0034")
    almere.naam.value should be("Almere")
    almere.aanvangGeldigheid.get.toString should be("1984-01-01")
    almere.eindeGeldigheid should be(None)
    almere.voortzettendeGemeente should be(None)
  }

  test("Gemeente Beerta") {
    val beerta = Gemeente("0006")

    beerta.code.value should be("0006")
    beerta.naam.value should be("Beerta")
    beerta.aanvangGeldigheid should be(None)
    beerta.eindeGeldigheid.get should be(Datum("1991-07-01"))
    beerta.voortzettendeGemeente.get.code.value should be("1661")

    val reiderland = beerta.voortzettendeGemeente.get
    reiderland.naam.value should be("Reiderland")
    val oldambt = reiderland.voortzettendeGemeente.get
    oldambt.naam.value should be("Oldambt")
    oldambt.voortzettendeGemeente should be(None)
  }
}
