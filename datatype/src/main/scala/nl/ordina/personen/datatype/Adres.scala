package nl.ordina.personen.datatype

import scala.io.Source
import scala.xml.XML

case class AdresIdentificatie(value: String) extends StringMetBeperkteLengte(value, 80)

case class NaamOpenbareRuimte(value: String) extends StringMetBeperkteLengte(value, 80)

case class Huisnummer(value: Int) extends NummerMetBeperkteLengte(value, 5)

case class Postcode(value: String) extends StringMetRegex(value, """[1-9][0-9][0-9][0-9][A-Z][A-Z]""".r) {
  assert(!(List("SS", "SA", "SD") contains value.substring(4)), s"$value is geen geldige postcode")

}

case class Adres(
  id: AdresIdentificatie,
  gemeente: Gemeente,
  straatNaam: NaamOpenbareRuimte,
  huisnummer: Huisnummer,
  postcode: Postcode
)

object Adres {
  private val xml = XML.load(Source.fromURL(getClass.getResource("Adres.xml")).reader())
  private val lijst: Map[String, Adres] = (xml \\ "adres").map {
    node =>
      val id = (node \ "id").text
      val gemeente = (node \ "gemeentenaam").text
      val straatNaam = (node \ "openbareruimtenaam").text
      val huisnummer = (node \ "huisnummer").text
      val postcode = (node \ "postcode").text
      Adres(id, gemeente, straatNaam, huisnummer.toInt, postcode)
  }.map(g => g.id.value -> g).toMap

  def apply(value: String): Adres = lijst(value)

  def apply(code: Gemeentecode): Adres = lijst(code.value)

  def apply(id: String, gemeente: String, straatNaam: String, huisnummer: Int, postcode: String): Adres = Adres(
    AdresIdentificatie(id),
    Gemeente(gemeente),
    NaamOpenbareRuimte(straatNaam),
    Huisnummer(huisnummer),
    Postcode(postcode)
  )
}