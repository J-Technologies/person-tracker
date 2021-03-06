package nl.ordina.personen.handlers.json

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.marshalling.ToResponseMarshallable
import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.model.headers.`Access-Control-Allow-Origin`
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import akka.stream.ActorMaterializer

import scala.io.StdIn


class WebServer extends PersoonJsonProtocol {

  implicit val system = ActorSystem("json")
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher

  def start() = {

    val interface = "localhost"
    val port = 8124
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
      complete("Welcome to JSON server")
    } ~
      pathPrefix("persoon") {
        (get & path(Segment)) { bsn =>
          respondWithHeaders(`Access-Control-Allow-Origin` *) {
            Option(persoonMapper.get(bsn)) match {
              case Some(person) => complete(ToResponseMarshallable(person))
              case None => complete(HttpResponse(NotFound))
            }
          }
        }
      }
}
