package nl.ordina.personen.datatype

case class SamengesteldeNaam(
  voornamen: Voornamen,
  voorvoegsel: Option[Voorvoegsel] = None,
  scheidingsteken: Option[Scheidingsteken] = None,
  geslachtsnaamstam: Geslachtsnaamstam,
  predicaat: Option[Predicaat] = None,
  titel: Option[AdellijkeTitel] = None
) {
  override def toString = {
    val achternaam = List(
      voorvoegsel, bepaalScheidingsteken, Some(geslachtsnaamstam)
    ).flatten.mkString
    List(predicaat, Some(voornamen), titel, Some(achternaam)).flatten.mkString(" ")
  }
  def bepaalScheidingsteken: Option[Scheidingsteken] =
    if (voorvoegsel != None)
      if (scheidingsteken == None) Some(Scheidingsteken(" "))
      else scheidingsteken
    else None
}

case class Voornamen(values: String*) {
  override def toString = values.mkString(" ")
}

case class Scheidingsteken(value: String) extends StringMetBeperkteLengte(value, 1)

case class Geslachtsnaamstam(value: String) extends StringMetBeperkteLengte(value, 200)

