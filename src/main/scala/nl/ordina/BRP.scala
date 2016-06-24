package nl.ordina

import java.util.concurrent.Executors

import nl.ordina.personen.command.GeboorteInNederland
import nl.ordina.personen.datatype._
import nl.ordina.personen.datatype.groep.Geboorte
import nl.ordina.personen.domein.entity.NatuurlijkPersoon
import org.axonframework.commandhandling.gateway.DefaultCommandGateway
import org.axonframework.commandhandling.{AggregateAnnotationCommandHandler, SimpleCommandBus}
import org.axonframework.eventhandling.{SimpleEventHandlerInvoker, SubscribingEventProcessor}
import org.axonframework.eventsourcing.EventSourcingRepository
import org.axonframework.eventsourcing.eventstore.EmbeddedEventStore
import org.axonframework.eventsourcing.eventstore.inmemory.InMemoryEventStorageEngine
import org.http4s.HttpService
import org.http4s.dsl.{->, /, Root, _}
import org.http4s.server.blaze.BlazeBuilder

object BRP {

  private implicit val scheduledEC = Executors.newScheduledThreadPool(4)

  val commandBus = new SimpleCommandBus()
  val commandGateway = new DefaultCommandGateway(commandBus)
  val eventStore = new EmbeddedEventStore(new InMemoryEventStorageEngine)
  val repo = new EventSourcingRepository[NatuurlijkPersoon](classOf[NatuurlijkPersoon], eventStore)

  def setupAxon() = {
    new SubscribingEventProcessor(new SimpleEventHandlerInvoker("test", new LogEventHandlers()), eventStore).start()
    new AggregateAnnotationCommandHandler(classOf[NatuurlijkPersoon], repo).subscribe(commandBus)
  }

  def setupBlazeServer() = {
    BlazeBuilder.bindHttp(8123)
      .mountService(
        HttpService {
          case GET -> Root / "geboorte" =>
            commandGateway.send(createNewGeboorte())
            Ok("Geboorte commando received")
        }, "/persoon"
      ).run.awaitShutdown
  }

  def main(args: Array[String]) {
    val commandHandlerRegistration = setupAxon()
    setupBlazeServer()
    commandHandlerRegistration.close()
  }

  def createNewGeboorte(): GeboorteInNederland = new GeboorteInNederland(
    Burgerservicenummer.nieuw,
    SamengesteldeNaam(Voornamen("Dirk"), Geslachtsnaam(Geslachtsnaamstam("Luijk"))),
    Geslachtsaanduiding.MAN,
    Geboorte(Datum("1993-01-01"), Gemeente("0505")),
    Partij("000505")
  )
}
