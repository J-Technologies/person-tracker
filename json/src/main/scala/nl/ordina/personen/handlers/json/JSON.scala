package nl.ordina.personen.handlers.json

/**
  * Created by gle21221 on 5-8-2016.
  */
object JSON {

  def main(args: Array[String]) {
    JsonEventHandler(jsonRepository)
    WebServer(jsonRepository).start()
  }

}
