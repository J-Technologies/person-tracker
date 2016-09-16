package nl.ordina.personen.command

import nl.ordina.personen.datatype.{Burgerservicenummer, Datum, Gemeente}

case class Huwelijk(gehuwden: List[Burgerservicenummer], datum: Datum, gemeente: Gemeente) {
  assert(gehuwden.size == 2)
}