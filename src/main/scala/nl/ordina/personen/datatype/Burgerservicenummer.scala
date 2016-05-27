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
}
object Burgerservicenummer {
  val LENGTE: Int = 9
  val SIGNIFICANT: Int = 8
  def elfproef(value: String): Boolean = value(SIGNIFICANT).asDigit == berekenSom(value)
  def nieuw: Burgerservicenummer = {
    val value: String = f"${Random.nextInt(99999999)}%08d"
    val som = berekenSom(value)
    if (som < 10) new Burgerservicenummer(value + som.toString)
    else nieuw
  }
  def berekenSom(value: String): Int =
    value.map(c => c.asDigit).take(SIGNIFICANT).zipWithIndex.map { case (cijfer, i) => cijfer * (LENGTE - i) }.sum % 11
}
