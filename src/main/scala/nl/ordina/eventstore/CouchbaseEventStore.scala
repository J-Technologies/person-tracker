package nl.ordina.eventstore

import org.axonframework.domain.{DomainEventMessage, DomainEventStream, SimpleDomainEventStream}
import org.axonframework.eventstore.EventStore
import org.axonframework.scynapse.serialization.xml.XStreamSerializer
import org.axonframework.serializer.SerializedObject
import org.reactivecouchbase.CouchbaseRWImplicits.jsObjectToDocumentWriter
import org.reactivecouchbase.ReactiveCouchbaseDriver
import play.api.libs.json.{JsObject, Json}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Success

class CouchbaseEventStore extends EventStore {

  val driver = ReactiveCouchbaseDriver()
  val bucket = driver.bucket("default")

  val s = new XStreamSerializer()

  override def readEvents(`type`: String, identifier: scala.Any): DomainEventStream = new SimpleDomainEventStream()

  override def appendEvents(`type`: String, events: DomainEventStream): Unit = {
    while (events.hasNext) {
      writeEvent(events.next())
    }
  }

  def writeEvent(event: DomainEventMessage[_]) = {
    val serializedPayLoad: SerializedObject[String] = s.serialize(event.getPayload, classOf[String])
    val serializedMetaData: SerializedObject[String] = s.serialize(event.getMetaData, classOf[String])

    bucket.set[JsObject](event.getAggregateIdentifier.toString, Json.obj(
      "identifier" -> event.getIdentifier,
      "timestamp" -> event.getTimestamp.toString,
      "aggregateIdentifier" -> event.getAggregateIdentifier.toString,
      "sequenceNumber" -> event.getSequenceNumber,

      "serializedPayloadLength" -> serializedPayLoad.getData.length,
      "serializedPayload" -> serializedPayLoad.getData,

      "serializedMetaDataLength" -> serializedMetaData.getData.length,
      "serializedMetaData" -> serializedMetaData.getData
    )).onComplete {
      case Success(success) => println(s"Operation successful {success.getMessage}")
      case _ => throw new Error
    }
  }

  sys.ShutdownHookThread {
    driver.shutdown()
  }
}