package nl.ordina.personen

package object datatypes {

  sealed abstract class Geslachtsaanduiding(val code: String, val naam: String){}

  case object MAN extends Geslachtsaanduiding("M", "Man")
  case object VROUW extends Geslachtsaanduiding("V", "Vrouw")
  case object ONBEKEND extends Geslachtsaanduiding("O", "Onbekend")

  case class Burgerservicenummer(value: String) {
    private val cijfers = value.map(c => c.asDigit)
    assert(cijfers.length == 9, "Een burgerservicenummer bestaat uit negen cijfers")
    assert(elfproef, s"$value voldoet niet aan de elfproef")

    private def elfproef = (
      cijfers
        .slice(0, 8)
        .zipWithIndex
        .map { case (cijfer, index) => (9 - index) * cijfer }
        .sum - cijfers(8)
      ) % 11 == 0
  }
}
