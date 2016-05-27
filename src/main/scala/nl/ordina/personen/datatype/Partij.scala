package nl.ordina.personen.datatype

case class Partij(
  code: Partijcode,
  naam: Partijnaam,
  soort: SoortPartij
)

case class Partijcode(value: String) extends StringMetBeperkteLengte(value, 6)
case class Partijnaam(value: String) extends StringMetBeperkteLengte(value, 80)
sealed abstract class SoortPartij(value: Int) extends Waardenlijst[Int](value, Set(1))

object SoortPartij {
  case object GEMEENTE extends SoortPartij(1)
}