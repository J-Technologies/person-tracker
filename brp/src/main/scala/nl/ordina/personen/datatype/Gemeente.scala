package nl.ordina.personen.datatype

import scala.io.Source
import scala.xml.XML

case class Gemeente(
  code: Gemeentecode,
  naam: Gemeentenaam,
  voortzettendeGemeenteCode: Option[Gemeentecode] = None,
  aanvangGeldigheid: Option[Datum] = None,
  eindeGeldigheid: Option[Datum] = None
) {
  lazy val voortzettendeGemeente: Option[Gemeente] = voortzettendeGemeenteCode.map(c => Gemeente(c.value))
}
object Gemeente {
  private val xml = XML.load(Source.fromURL(getClass.getResource("Gemeente.xml")).reader())
  private val lijst: Map[String, Gemeente] = (xml \\ "gemeente").map {
    node =>
      val code = (node \ "code").text
      val naam = (node \ "naam").text
      val nieuw = (node \ "nieuw").text
      val aanvang = (node \ "aanvang").text
      val einde = (node \ "einde").text
      Gemeente(
        Gemeentecode(code),
        Gemeentenaam(naam),
        if (nieuw.nonEmpty) Some(Gemeentecode(nieuw)) else None,
        if (aanvang.nonEmpty) Some(Datum(aanvang)) else None,
        if (einde.nonEmpty) Some(Datum(einde)) else None
      )
  }.map(g => g.code.value -> g).toMap
  def apply(value: String): Gemeente = lijst(value)
  def apply(code: Gemeentecode): Gemeente = lijst(code.value)
}

case class Gemeentecode(value: String) extends StringMetBeperkteLengte(value, 4)
case class Gemeentenaam(value: String) extends StringMetBeperkteLengte(value, 80)


