package nl.ordina.personen

import java.io.PrintWriter
import java.sql.Connection
import java.util.Properties

import nl.ordina.personen.command.{GeboorteInNederland, OverlijdenPersoon}
import nl.ordina.personen.datatype._
import nl.ordina.personen.datatype.groep.{Geboorte, Overlijden}
import nl.ordina.personen.domein.entity.NatuurlijkPersoon
import org.apache.derby.drda.NetworkServerControl
import org.apache.derby.jdbc.EmbeddedDriver
import org.axonframework.commandhandling.gateway.DefaultCommandGateway
import org.axonframework.commandhandling.{AggregateAnnotationCommandHandler, SimpleCommandBus}
import org.axonframework.eventsourcing.EventSourcingRepository

object BRP {

  def startDerbyNetworkServer(): Connection = {
    new NetworkServerControl().start(new PrintWriter(System.out))
    new EmbeddedDriver().connect("jdbc:derby:target/db;create=true", new Properties())
  }

  def main(args: Array[String]) {
    startDerbyNetworkServer()
    val repository = new EventSourcingRepository[NatuurlijkPersoon](classOf[NatuurlijkPersoon], event.eventStore)
    val commandBus = new SimpleCommandBus() {
      setTransactionManager(event.transactionManager)
    }
    val commandGateway = new DefaultCommandGateway(commandBus)
    val commandHandler = new AggregateAnnotationCommandHandler(classOf[NatuurlijkPersoon], repository)
    val commandHandlerRegistration = commandHandler.subscribe(commandBus)

    val burgerservicenummer = Burgerservicenummer.nieuw
    commandGateway.sendAndWait(
      GeboorteInNederland(
        burgerservicenummer,
        SamengesteldeNaam(Voornamen("Gerard"), Geslachtsnaam(Geslachtsnaamstam("Leeuw"), Option(Voorvoegsel("de")))),
        Geslachtsaanduiding.MAN,
        Geboorte(Datum.gisteren, Gemeente("1896")),
        Partij("000505")
      )
    )
    commandGateway.sendAndWait(
      OverlijdenPersoon(
        burgerservicenummer,
        Overlijden(Datum.vandaag, Gemeente("1896"))
      )
    )

    WebServer(commandGateway).start()
    commandHandlerRegistration.close()
  }
}
