package nl.ordina.personen.handlers.json

import java.io.PrintWriter
import java.net.InetAddress
import java.sql.Connection
import java.util.Properties

import org.apache.derby.drda.NetworkServerControl
import org.apache.derby.jdbc.EmbeddedDriver

/**
  * Created by gle21221 on 5-8-2016.
  */
object JSON {

  def startDerbyNetworkServer(): Connection = {
    val derbyPortNumber = 1528
    new NetworkServerControl(InetAddress.getByAddress(Array[Byte](0, 0, 0, 0)), derbyPortNumber).start(new PrintWriter(System.out))
    new EmbeddedDriver().connect("jdbc:derby:target/json;create=true", new Properties())
  }

  def main(args: Array[String]) {
    startDerbyNetworkServer()
    JsonEventHandler(jsonRepository)
    WebServer(jsonRepository).start()
  }

}
