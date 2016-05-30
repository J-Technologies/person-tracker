package nl.ordina.personen.datatype

import nl.ordina.personen.{BRAL0012, controle}

import scala.util.Random

case class Burgerservicenummer(value: String) {
  assert(value.length == 9, s"Een burgerservicenummer heeft negen cijfers; $value heeft er ${value.length}")
  controle(Burgerservicenummer.elfproef(value), BRAL0012, value)
  override def toString = value
}
object Burgerservicenummer {
  val random = Random
  def elfproef(value: String): Boolean = value.last.asDigit == checkDigit(value)
  def nieuw: Burgerservicenummer = {
    val value = f"${random.nextInt(100000000)}%08d"
    val cd = checkDigit(value)
    if (cd < 10) new Burgerservicenummer(value + cd.toString) else nieuw
  }
  def checkDigit(v: String) = v.map(_.asDigit).take(8).reverse.zipWithIndex.map { case (d, i) => d * (i + 2) }.sum % 11
}
