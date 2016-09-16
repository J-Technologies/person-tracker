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
  private val codeLijst: Map[String, Gemeente] = (xml \\ "gemeente").map {
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
  private val naamLijst: Map[String, Gemeente] = codeLijst.map(g => g._2.naam.value -> g._2)

  def apply(value: String): Gemeente = codeLijst.getOrElse(value, naamLijst(value))

  def apply(code: Gemeentecode): Gemeente = codeLijst(code.value)

  def apply(naam: Gemeentenaam): Gemeente = naamLijst(naam.value)
}

case class Gemeentecode(value: String) extends StringMetBeperkteLengte(value, 4)

case class Gemeentenaam(value: String) extends StringMetBeperkteLengte(value, 80)


