package nl.ordina.personen.datatype

import scala.io.Source
import scala.xml.XML

case class Gemeente(
  val code: Gemeentecode,
  val naam: Gemeentenaam,
  val voortzettendeGemeenteCode: Option[Gemeentecode] = None,
  val aanvangGeldigheid: Option[Datum] = None,
  val eindeGeldigheid: Option[Datum] = None
) {
  def voortzettendeGemeente = voortzettendeGemeenteCode.map(c => Gemeente(c.value))
}
object Gemeente {
  private val lijst = GemeenteLijst.lijst
  def apply(value: String) = lijst(value)
  def apply(code: Gemeentecode) = lijst(code.value)
}
object GemeenteLijst {
  private val xml = XML.load(Source.fromURL(getClass.getResource("/Gemeente.xml")).reader())
  val lijst: Map[String, Gemeente] = (xml \\ "gemeente").map { node =>
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
}

case class Gemeentecode(val value: String) extends StringMetBeperkteLengte(value, 4)
case class Gemeentenaam(val value: String) extends StringMetBeperkteLengte(value, 80)
case class Partij(val value: String, val naam: String)
