package nl.ordina.personen

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.model.headers.`Access-Control-Allow-Origin`
import akka.http.scaladsl.model.ws.{Message, TextMessage}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Flow, Sink, Source}
import akka.util.Timeout
import nl.ordina.personen.command.{GeboorteInNederland, Huwelijk, OverlijdenPersoon}
import nl.ordina.personen.datatype.groep.{Geboorte, Overlijden}
import nl.ordina.personen.datatype.{Datum, Gemeente, Geslachtsaanduiding, Partij, _}
import nl.ordina.personen.handlers.EventHandlerActor
import org.axonframework.commandhandling.gateway.CommandGateway

import scala.concurrent.duration._
import scala.io.StdIn

class WebServer(commandGateway: CommandGateway) {

  implicit val system = ActorSystem("brp")
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher
  implicit val timeout = Timeout(1 second)

  val eventSource: Source[Message, Any] = Source.actorPublisher[Message](EventHandlerActor.props)
  val eventSink: Sink[Message, Any] = Sink.foreach(println)
  val eventFlow: Flow[Message, Message, Any] = Flow.fromSinkAndSource(eventSink, eventSource)

  val echoService: Flow[Message, Message, _] = Flow[Message].map {
    case TextMessage.Strict(txt) => TextMessage("ECHO: " + txt)
    case _ => TextMessage("Message type unsupported")
  }

  def start() = {

    val interface = "localhost"
    val port = 8123
    val bindingFuture = Http().bindAndHandle(route, interface, port)

    println(s"Server is now online at http://$interface:$port/\nPress RETURN to stop...")
    StdIn.readLine() // let it run until user presses return
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done
    println("Server is down...")
  }

  def route: Route =
    pathEndOrSingleSlash {
      complete("Welcome to websocket server")
    } ~
      path("ws-echo") {
        get {
          handleWebSocketMessages(echoService)
        }
      } ~
      pathPrefix("persoon") {
        path("geboorte") {
          post {
            formFieldMap { fields =>
              createNewGeboorte(
                fields.getOrElse("voornaam", ""),
                fields.getOrElse("achternaam", ""),
                fields.getOrElse("geboortedatum", ""),
                Geslachtsaanduiding.fromString(fields.getOrElse("geslacht", "")),
                List(fields.getOrElse("ouder1", ""), fields.getOrElse("ouder2", "")),
                fields.getOrElse("gemeente", ""),
                fields.getOrElse("partij", "")
              )

              complete(HttpResponse()
                .withEntity("Geboorte commando received")
                .withHeaders(`Access-Control-Allow-Origin` *))
            }
          }
        } ~ path("overlijden") {
          post {
            formFieldMap { fields =>
              createOverlijden(
                fields.getOrElse("bsn", ""),
                fields.getOrElse("datum", ""),
                fields.getOrElse("gemeente", "")
              )
              complete(HttpResponse()
                .withEntity("Overlijden commando received")
                .withHeaders(`Access-Control-Allow-Origin` *))
            }
          }

        } ~ path("huwelijk") {
          post {
            formFieldMap { fields =>
              createHuwelijk(
                fields.getOrElse("bsn1", ""),
                fields.getOrElse("bsn2", ""),
                fields.getOrElse("datum", ""),
                fields.getOrElse("gemeente", "")
              )
              complete(HttpResponse()
                .withEntity("Huwelijk commando received")
                .withHeaders(`Access-Control-Allow-Origin` *))
            }
          }

        } ~
          path("websocket") {
            handleWebSocketMessages(eventFlow)
          }
      }

  def createNewGeboorte(voornaam: String,
    achternaam: String,
    geboortedatum: String,
    geslacht: Geslachtsaanduiding,
    ouders: List[String],
    gemeente: String,
    partij: String
  ): Unit =
    commandGateway.sendAndWait(
      GeboorteInNederland(
        Burgerservicenummer.nieuw,
        SamengesteldeNaam(Voornamen(voornaam), Geslachtsnaam(Geslachtsnaamstam(achternaam))),
        geslacht,
        Geboorte(Datum(geboortedatum), Gemeente(gemeente)),
        ouders.filter(s => s.nonEmpty).map(s => Burgerservicenummer(s)),
        null,
        Partij(partij)
      )
    )

  def createOverlijden(bsn: String, datum: String, gemeente: String): Unit =
    commandGateway.sendAndWait(
      OverlijdenPersoon(
        burgerservicenummer = Burgerservicenummer(bsn),
        overlijden = Overlijden(Datum(datum), Gemeente(gemeente))
      )
    )

  def createHuwelijk(bsn1: String, bsn2: String, datum: String, gemeente: String): Unit = {
    commandGateway.sendAndWait(
      Huwelijk(
        burgerservicenummer = Burgerservicenummer(bsn1),
        partner = Burgerservicenummer(bsn2),
        datum = Datum(datum),
        gemeente = Gemeente(gemeente)
      )
    )
    commandGateway.sendAndWait(
      Huwelijk(
        burgerservicenummer = Burgerservicenummer(bsn2),
        partner = Burgerservicenummer(bsn1),
        datum = Datum(datum),
        gemeente = Gemeente(gemeente)
      )
    )
  }
}
