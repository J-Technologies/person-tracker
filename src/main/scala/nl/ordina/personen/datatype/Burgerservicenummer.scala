package nl.ordina.personen.datatype

import nl.ordina.personen.{BRAL0012, controle}

import scala.util.Random

case class Burgerservicenummer(value: String) {
  assert(value.length == 9, s"Een burgerservicenummer heeft negen cijfers; $value heeft er ${value.length}")
  controle(Burgerservicenummer.elfproef(value), BRAL0012, value)
  override def toString = value
}

object Burgerservicenummer {
  def elfproef(value: String) = som(value.take(8)) == value.last.asDigit
  def nieuw: Burgerservicenummer = {
    val value = f"${Random.nextInt(100000000)}%08d"
    val cd = som(value)
    if (cd < 10) new Burgerservicenummer(value + cd) else nieuw
  }
  def som(v: String) = v.map(_.asDigit).reverse.zipWithIndex.map { case (d, i) => d * (i + 2) }.sum % 11
}
