package nl.ordina.personen.datatypes

/**
  * @author Eric Malotaux
  * @date 4/29/16.
  */
object Geslachtsaanduiding extends Enumeration {

  case class GeslachtsaanduidingWaarde(code: String, naam: String) extends Val

  val MAN = GeslachtsaanduidingWaarde("M", "Man")
  val VROUW = GeslachtsaanduidingWaarde("V", "Vrouw")
  val ONBEKEND = GeslachtsaanduidingWaarde("O", "Onbekend")
}

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

  override def toString: String = value
}

