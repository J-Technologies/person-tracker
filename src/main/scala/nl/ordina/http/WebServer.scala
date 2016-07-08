package nl.ordina.http

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
import nl.ordina.EventHandlerActor
import nl.ordina.personen.command.GeboorteInNederland
import nl.ordina.personen.datatype.groep.Geboorte
import nl.ordina.personen.datatype.{Datum, Gemeente, Geslachtsaanduiding, Partij, _}
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.eventsourcing.eventstore.EventStore

import scala.concurrent.duration._
import scala.io.StdIn

class WebServer(eventStore: EventStore, commandGateway: CommandGateway) {

  implicit val system = ActorSystem("webapi")
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher
  implicit val timeout = Timeout(1 second)

  val eventSource: Source[Message, Any] = Source.actorPublisher[Message](EventHandlerActor.props(eventStore))
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
              commandGateway.sendAndWait(createNewGeboorte(fields.getOrElse("voornaam", ""), fields.getOrElse("achternaam", "")))
              complete(HttpResponse()
                .withEntity("Geboorte commando received")
                .withHeaders(`Access-Control-Allow-Origin` *))
            }
          }
        } ~
          path("websocket") {
            handleWebSocketMessages(eventFlow)
          }
      }

  def createNewGeboorte(voornaam: String, achternaam: String): GeboorteInNederland = new GeboorteInNederland(
    Burgerservicenummer.nieuw,
    SamengesteldeNaam(Voornamen(voornaam), Geslachtsnaam(Geslachtsnaamstam(achternaam))),
    Geslachtsaanduiding.MAN,
    Geboorte(Datum("1993-01-01"), Gemeente("0505")),
    Partij("000505")
  )
}
