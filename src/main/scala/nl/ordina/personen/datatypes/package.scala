package nl.ordina.personen

package object datatypes {

  sealed abstract class Geslachtsaanduiding(val code: String, val naam: String)
  case object MAN extends Geslachtsaanduiding("M", "Man")
  case object VROUW extends Geslachtsaanduiding("V", "Vrouw")
  case object ONBEKEND extends Geslachtsaanduiding("O", "Onbekend")

  case class Burgerservicenummer(value: String) {
    assert(value.length == 9, "Een burgerservicenummer heeft negen cijfers")
    private val cijfers = value.map(c => c.asDigit)
    private val controlecijfer = cijfers(8)
    private val som = cijfers.take(8).zipWithIndex.map(e => (9 - e._2) * e._1).sum
    private val elfproef = controlecijfer == som % 11
    assert(elfproef, s"$value is geen geldig burgerservicenummer")
  }
  object Burgerservicenummer {
    def apply(value: Int) = new Burgerservicenummer(f"$value%09d")
  }

}
