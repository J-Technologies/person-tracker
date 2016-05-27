package nl.ordina.personen.datatype

import scala.io.Source
import scala.xml.XML

case class Partij(
  code: Partijcode,
  naam: Partijnaam,
  soort: SoortPartij
)

case class Partijcode(value: String) extends StringMetBeperkteLengte(value, 6)
case class Partijnaam(value: String) extends StringMetBeperkteLengte(value, 80)
sealed case class SoortPartij(value: Int, naam: String)

object SoortPartij {
  object GEMEENTE extends SoortPartij(1, "Gemeente")
  val lijst: Map[Int, SoortPartij] = Map(GEMEENTE.value -> GEMEENTE)
  def apply(value: Int) = lijst(value)
}

object Partij {
  private val xml = XML.load(Source.fromURL(getClass.getResource("Partij.xml")).reader())
  private val lijst: Map[String, Partij] = (xml \\ "partij").map {
    node =>
      val code = (node \ "code").text
      val naam = (node \ "naam").text
      val soort = (node \ "soort").text.toInt
      Partij(
        Partijcode(code),
        Partijnaam(naam),
        SoortPartij(soort)
      )
  }.map(g => g.code.value -> g).toMap
  def apply(value: String): Partij = lijst(value)
  def apply(code: Partijcode): Partij = lijst(code.value)
}