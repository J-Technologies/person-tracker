package nl.ordina.personen.datatype

sealed abstract class AdellijkeTitel(val code: String, val naam: String) {
  override def toString: String = naam
}
object AdellijkeTitel {
  object BARON extends AdellijkeTitel("B", "Baron")
  object BARONES extends AdellijkeTitel("BS", "Barones")
  object GRAAF extends AdellijkeTitel("G", "Graaf")
  object GRAVIN extends AdellijkeTitel("GI", "Gravin")
  object HERTOG extends AdellijkeTitel("H", "Hertog")
  object HERTOGIN extends AdellijkeTitel("HI", "Hertogin")
  object MARKIES extends AdellijkeTitel("M", "Markies")
  object MARKIEZIN extends AdellijkeTitel("MI", "Markiezin")
  object PRINS extends AdellijkeTitel("P", "Prins")
  object PRINSES extends AdellijkeTitel("PS", "Prinses")
  object RIDDER extends AdellijkeTitel("R", "Ridder")
  private val lijst: Map[String, AdellijkeTitel] = List[AdellijkeTitel](
    BARON,
    BARONES,
    GRAAF,
    GRAVIN,
    HERTOG,
    HERTOGIN,
    MARKIES,
    MARKIEZIN,
    PRINS,
    PRINSES,
    RIDDER
  ).map(t => t.code -> t).toMap
  def apply(code: String): AdellijkeTitel = lijst(code)
}
