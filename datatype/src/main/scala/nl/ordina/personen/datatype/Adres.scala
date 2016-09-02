package nl.ordina.personen.datatype

case class AdresIdentificatie(value: String) extends StringMetBeperkteLengte(value, 16)
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
  def apply(id: String, gemeente: String, straatNaam: String, huisnummer: Int, postcode: String): Adres = Adres(
    AdresIdentificatie(id),
    Gemeente(gemeente),
    NaamOpenbareRuimte(straatNaam),
    Huisnummer(huisnummer),
    Postcode(postcode)
  )
}