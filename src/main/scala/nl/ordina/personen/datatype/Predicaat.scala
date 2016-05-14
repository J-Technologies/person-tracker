package nl.ordina.personen.datatype

sealed abstract class Predicaat(code: String, naam: String) {
  override def toString: String = naam
}
object Predicaat {
  object JONKHEER extends Predicaat("JH", "Jonkheer")
  object JONKVROUW extends Predicaat("JV", "Jonkvrouw")
}
