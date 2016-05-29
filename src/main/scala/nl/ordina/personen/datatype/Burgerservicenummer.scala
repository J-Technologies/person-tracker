/**
  * Copyright (C) 2016 Ordina
  */

package nl.ordina.personen.datatype

import nl.ordina.personen.{BRAL0012, controle}

import scala.util.Random

case class Burgerservicenummer(value: String) {
  assert(
    value.length == Burgerservicenummer.LENGTE,
    s"Een burgerservicenummer heeft negen cijfers; $value heeft er ${value.length}"
  )
  controle(Burgerservicenummer.elfproef(value), BRAL0012, value)
  override def toString = value
}
object Burgerservicenummer {
  val LENGTE: Int = 9
  val SIGNIFICANT: Int = 8
  val random = Random
  def elfproef(value: String): Boolean = value(SIGNIFICANT).asDigit == chk(value)
  def nieuw: Burgerservicenummer = {
    val value: String = f"${random.nextInt(100000000)}%08d"
    val checkDigit = chk(value)
    if (checkDigit < 10) new Burgerservicenummer(value + checkDigit.toString)
    else nieuw
  }
  def chk(v: String) = v.map(_.asDigit).take(SIGNIFICANT).zipWithIndex.map { case (d, i) => d * (LENGTE - i) }.sum % 11
}
