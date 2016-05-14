package nl.ordina.personen.datatype

case class SamengesteldeNaam(
  voornamen: Voornamen,
  voorvoegsel: Option[Voorvoegsel] = None,
  scheidingsteken: Option[Scheidingsteken] = None,
  geslachtsnaamstam: Geslachtsnaamstam
) {
  override def toString = {
    val achternaam = List(
      voorvoegsel, bepaalScheidingsteken, Some(geslachtsnaamstam)
    ).flatten.mkString
    s"$voornamen $achternaam"
  }
  def bepaalScheidingsteken: Option[Scheidingsteken] =
    if (voorvoegsel != None)
      if (scheidingsteken == None) Some(Scheidingsteken(" "))
      else scheidingsteken
    else None
}

object SamengesteldeNaam {
  def apply(
    voornamen: Voornamen,
    voorvoegsel: Voorvoegsel,
    scheidingsteken: Scheidingsteken,
    geslachtsnaamstam: Geslachtsnaamstam
  ): SamengesteldeNaam = apply(voornamen, Some(voorvoegsel), Some(scheidingsteken), geslachtsnaamstam)
}

case class Voornamen(values: String*) {
  override def toString = values.mkString(" ")
}

case class Scheidingsteken(value: String) extends StringMetBeperkteLengte(value, 1)

case class Geslachtsnaamstam(value: String) extends StringMetBeperkteLengte(value, 200)

