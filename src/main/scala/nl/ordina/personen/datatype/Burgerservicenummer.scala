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
  def apply(value: Int): Burgerservicenummer = new Burgerservicenummer(f"$value%09d")
  val LENGTE: Int = 9
  val SIGNIFICANTE_LENGTE: Int = 8
  def elfproef(value: String): Boolean = {
    val cijfers = value.map(c => c.asDigit)
    val controlecijfer = cijfers(SIGNIFICANTE_LENGTE)
    val producten = cijfers.take(SIGNIFICANTE_LENGTE).zipWithIndex.map { case (cijfer, i) => cijfer * (LENGTE - i) }
    controlecijfer == producten.sum % 11
  }
  def nieuw: Burgerservicenummer = {
    val random: Int = Random.nextInt(99999999)
    val cijfers = f"$random%08d".map(c => c.asDigit)
    val producten = cijfers.zipWithIndex.map { case (cijfer, i) => cijfer * (LENGTE - i) }
    val controlecijfer = producten.sum % 11
    if (controlecijfer < 10)
      new Burgerservicenummer((cijfers :+ controlecijfer).mkString)
    else nieuw
  }
}
