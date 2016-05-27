package nl.ordina.eventstore

import java.util

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import org.axonframework.domain.{DomainEventMessage, DomainEventStream, GenericDomainEventMessage, SimpleDomainEventStream}
import org.axonframework.eventstore.EventStore
import org.joda.time.DateTime
import org.reactivecouchbase.CouchbaseRWImplicits.{documentAsJsObjectReader, jsObjectToDocumentWriter}
import org.reactivecouchbase.ReactiveCouchbaseDriver
import play.api.libs.json._

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.util.Success

class CouchbaseEventStore extends EventStore {

  val driver = ReactiveCouchbaseDriver()
  val bucket = driver.bucket("default")

  val mapper = new ObjectMapper()
  mapper.registerModule(DefaultScalaModule)

  override def readEvents(`type`: String, identifier: Any): DomainEventStream = {
    val event = Await.result(bucket.get[JsObject](identifier.toString), Duration.Undefined).get // Kill All the Unicorns!
    val timestamp = (event \ "timestamp").as[DateTime]
    val sequenceNumber = (event \ "sequenceNumber").as[Long]

    val classType = Class.forName(`type`)

    val payload = (event \ "serializedPayload").as[classType.type]
    val metadata = (event \ "serializedMetaData").as[util.HashMap[String, Any]]

    new SimpleDomainEventStream(new GenericDomainEventMessage[classType.type](`type`, timestamp, identifier, sequenceNumber, payload, metadata))
  }

  override def appendEvents(`type`: String, events: DomainEventStream): Unit = {
    while (events.hasNext) {
      writeEvent(events.next())
    }
  }

  private def writeEvent(event: DomainEventMessage[_]): Unit = {
    bucket.set[JsObject](event.getAggregateIdentifier.toString, Json.obj(
      "identifier" -> event.getIdentifier,
      "timestamp" -> event.getTimestamp.toString,
      "aggregateIdentifier" -> event.getAggregateIdentifier.toString,
      "sequenceNumber" -> event.getSequenceNumber,

      "serializedPayload" -> mapper.writeValueAsString(event.getPayload),
      "serializedMetaData" -> mapper.writeValueAsString(event.getMetaData)
    )).onComplete {
      case Success(success) => println(s"Operation successful {success.getMessage}")
      case _ => throw new Error
    }
  }

  sys.ShutdownHookThread {
    driver.shutdown()
  }
}