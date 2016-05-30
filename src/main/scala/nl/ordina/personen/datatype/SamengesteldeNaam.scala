package nl.ordina.personen.datatype

import nl.ordina.personen.datatype.JaOfNee.Ja

case class SamengesteldeNaam(voornamen: Voornamen, geslachtsnaam: Geslachtsnaam) {
  val afgeleid = Ja
  override lazy val toString = List(geslachtsnaam.predicaat, Some(voornamen), geslachtsnaam.titel, Some(geslachtsnaam))
    .flatten.mkString(" ")
}

case class Voornamen(values: String*) {
  assert(values.map(v => v.length).sum <= 200, "voornamen mogen samen niet langer dan 200 tekens zijn")
  override def toString = values.mkString(" ")
}

case class Scheidingsteken(value: String = " ") extends StringMetBeperkteLengte(value, 1)

case class Geslachtsnaamstam(value: String) extends StringMetBeperkteLengte(value, 200)
case class Geslachtsnaam(
  stam: Geslachtsnaamstam,
  voorvoegsel: Option[Voorvoegsel] = None,
  scheidingsteken: Scheidingsteken = Scheidingsteken(),
  predicaat: Option[Predicaat] = None,
  titel: Option[AdellijkeTitel] = None
) {
  override def toString = List(voorvoegsel, bepaalScheidingsteken, Some(stam)).flatten.mkString
  lazy val bepaalScheidingsteken: Option[Scheidingsteken] =
    if (voorvoegsel.isDefined) Some(scheidingsteken)
    else None
}
