package nl.ordina

package object personen {
  sealed case class ErnstControleRegel(code: String, naam: String)
  object BLOKKEREND extends ErnstControleRegel("B", "Blokkerend")
  object DEBLOKKEERBAAR extends ErnstControleRegel("D", "Deblokkeerbaar")
  object WAARSCHUWING extends ErnstControleRegel("W", "Waarschuwing")

  sealed case class ControleRegel(code: String, omschrijving: String, ernst: ErnstControleRegel)
  object BRAL0012 extends ControleRegel("BRAL0012", "%s moet voldoen aan de elfproef", BLOKKEREND)

  case class ControleRegelException(regel: ControleRegel, argumenten: Any*) extends Exception(regel.omschrijving) {
    override def getMessage: String = {
      super.getMessage.format(argumenten : _*)
    }
  }

  def controle(assertion: Boolean, regel: ControleRegel, argumenten: Any*): Unit = {
    if (!assertion) throw new ControleRegelException(regel, argumenten: _*)
  }
}
