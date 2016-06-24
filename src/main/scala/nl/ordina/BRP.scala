package nl.ordina

import java.util.concurrent.Executors

import nl.ordina.eventstore.CouchbaseEventStore
import nl.ordina.personen.command.GeboorteInNederland
import nl.ordina.personen.datatype._
import nl.ordina.personen.datatype.groep.Geboorte
import nl.ordina.personen.domein.entity.NatuurlijkPersoon
import org.axonframework.commandhandling.SimpleCommandBus
import org.axonframework.commandhandling.annotation.AggregateAnnotationCommandHandler
import org.axonframework.commandhandling.gateway.DefaultCommandGateway
import org.axonframework.eventhandling.SimpleEventBus
import org.axonframework.eventhandling.annotation.AnnotationEventListenerAdapter
import org.axonframework.eventsourcing.EventSourcingRepository
import org.http4s.HttpService
import org.http4s.dsl.{->, /, Root, _}
import org.http4s.server.blaze.BlazeBuilder

object BRP {

  private implicit val scheduledEC = Executors.newScheduledThreadPool(4)

  val commandBus = new SimpleCommandBus()
  val commandGateway = new DefaultCommandGateway(commandBus)
  val eventBus = new SimpleEventBus()
  val repo = new EventSourcingRepository[NatuurlijkPersoon](classOf[NatuurlijkPersoon], new CouchbaseEventStore)

  val natuurlijkPeroon: NatuurlijkPersoon = new NatuurlijkPersoon()

  def setupAxon() = {
    repo.setEventBus(eventBus)
    AggregateAnnotationCommandHandler.subscribe(classOf[NatuurlijkPersoon], repo, commandBus)
    AnnotationEventListenerAdapter.subscribe(natuurlijkPeroon, eventBus)
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
    setupAxon()
    setupBlazeServer()
  }

  def createNewGeboorte(): GeboorteInNederland = new GeboorteInNederland(
    Burgerservicenummer.nieuw,
    SamengesteldeNaam(Voornamen("Dirk"), Geslachtsnaam(Geslachtsnaamstam("Luijk"))),
    Geslachtsaanduiding.MAN,
    Geboorte(Datum("1993-01-01"), Gemeente("0505")),
    Partij("000505")
  )
}
