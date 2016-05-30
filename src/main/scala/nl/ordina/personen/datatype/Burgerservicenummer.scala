package nl.ordina.personen.datatype

import nl.ordina.personen.{BRAL0012, controle}

import scala.util.Random

case class Burgerservicenummer(value: String) extends StringMetVasteLengte(value, 9) {
  controle(Burgerservicenummer.elfproef(value), BRAL0012, value)
}

object Burgerservicenummer {
  def elfproef(value: String) = som(value.take(8)) == value.last.asDigit
  def nieuw: Burgerservicenummer = {
    val value = f"${Random.nextInt(100000000)}%08d"
    val cd = som(value)
    if (cd < 10) new Burgerservicenummer(value + cd) else nieuw
  }
  def som(v: String) = v.map(_.asDigit).zip(9 to (2, -1)).map { case (d, i) => d * i }.sum % 11
}
