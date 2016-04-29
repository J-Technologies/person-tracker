package nl.ordina.personen

package object datatypes {

  sealed abstract class Geslachtsaanduiding(val code: String, val naam: String)
  case object MAN extends Geslachtsaanduiding("M", "Man")
  case object VROUW extends Geslachtsaanduiding("V", "Vrouw")
  case object ONBEKEND extends Geslachtsaanduiding("O", "Onbekend")

  case class Burgerservicenummer(value: String) {
    assert(value.length == 9, s"Een burgerservicenummer heeft negen cijfers; $value heeft er ${value.length}")
    private val elfproef = {
      val cijfers = value.map(c => c.asDigit)
      val controlecijfer = cijfers(8)
      val som = cijfers.take(8).zipWithIndex.map(e => e._1 * (9 - e._2)).sum
      controlecijfer == som % 11
    }
    assert(elfproef, s"$value is geen geldig burgerservicenummer")
  }
  object Burgerservicenummer {
    def apply(value: Int) = new Burgerservicenummer(f"$value%09d")
  }

}
