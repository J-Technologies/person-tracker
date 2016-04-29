package nl.ordina.personen

package object datatypes {

  sealed abstract class Geslachtsaanduiding(val code: String, val naam: String)
  case object MAN extends Geslachtsaanduiding("M", "Man")
  case object VROUW extends Geslachtsaanduiding("V", "Vrouw")
  case object ONBEKEND extends Geslachtsaanduiding("O", "Onbekend")

  case class Burgerservicenummer(value: String) {
    assert(value.length == 9, "Een burgerservicenummer heeft negen cijfers")
    private val nummer = value.map(c => c.asDigit)
    private val elfproef = (nummer.slice(0, 8).zipWithIndex.map(e => (9 - e._2) * e._1).sum - nummer(8)) % 11 == 0
    assert(elfproef, s"$value is geen geldig burgerservicenummer")
  }
  object Burgerservicenummer {
    def apply(value: Int) = new Burgerservicenummer(f"$value%09d")
  }

}
