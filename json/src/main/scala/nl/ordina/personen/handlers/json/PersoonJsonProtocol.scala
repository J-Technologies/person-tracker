package nl.ordina.personen.handlers.json

import java.util

import spray.json.{DefaultJsonProtocol, JsArray, JsValue, JsonFormat, RootJsonFormat, _}

import scala.collection.JavaConversions._

trait PersoonJsonProtocol extends DefaultJsonProtocol {
  /**
    * Supplies the JsonFormat for Lists.
    */
  implicit def javaListFormat[T: JsonFormat] = new RootJsonFormat[util.List[T]] {
    def write(list: util.List[T]) = JsArray(list.map(_.toJson).toVector)

    def read(value: JsValue): util.List[T] = value match {
      case JsArray(elements) => elements.map(_.convertTo[T]).toList
      case x => deserializationError("Expected List as JsArray, but got " + x)
    }
  }

  implicit val persoonEntryFormat = jsonFormat8(PersoonEntry)
}
