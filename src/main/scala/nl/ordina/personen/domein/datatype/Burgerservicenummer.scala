package nl.ordina.personen.domein.datatype

/**
	* @author Eric Malotaux
	* @date 5/13/16.
	*/

case class Burgerservicenummer(value: String) {
	assert(value.length == 9, s"Een burgerservicenummer heeft negen cijfers; $value heeft er ${value.length}")
	assert(Burgerservicenummer.elfproef(value), s"$value is geen geldig burgerservicenummer")
}
object Burgerservicenummer {
	def apply(value: Int) = new Burgerservicenummer(f"$value%09d")
	def elfproef(value: String): Boolean = {
		val cijfers = value.map(c => c.asDigit)
		val controlecijfer = cijfers(8)
		val som = cijfers.take(8).zipWithIndex.map(e => e._1 * (9 - e._2)).sum
		controlecijfer == som % 11
	}
	def elfproef(value: Int): Boolean = elfproef(f"$value%09d")
}
