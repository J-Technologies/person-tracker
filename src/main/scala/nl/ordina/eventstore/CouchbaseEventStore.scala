package nl.ordina.eventstore

import java.util

import org.axonframework.domain.{DomainEventMessage, DomainEventStream, GenericDomainEventMessage, SimpleDomainEventStream}
import org.axonframework.eventstore.EventStore
import org.axonframework.scynapse.serialization.xml.XStreamSerializer
import org.axonframework.serializer.{SerializedObject, SimpleSerializedObject}
import org.joda.time.DateTime
import org.reactivecouchbase.CouchbaseRWImplicits.jsObjectToDocumentWriter
import org.reactivecouchbase.CouchbaseRWImplicits.documentAsJsObjectReader
import org.reactivecouchbase.ReactiveCouchbaseDriver
import play.api.libs.json._

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.util.Success

class CouchbaseEventStore extends EventStore {

  val s = new XStreamSerializer()

  val driver = ReactiveCouchbaseDriver()
  val bucket = driver.bucket("default")

  override def readEvents(`type`: String, identifier: Any): DomainEventStream = {
    val event = Await.result(bucket.get[JsObject](identifier.toString), Duration.Undefined).get // Kill All the Unicorns!
    val timestamp = (event \ "timestamp").as[DateTime]
    val sequenceNumber = (event \ "sequenceNumber").as[Long]

    val serializedPayload = new SimpleSerializedObject(
      (event \ "serializedPayload").as[String],
      classOf[String],
      "ignored", "0")
    val classType = Class.forName(`type`)
    val payload = s.deserialize(serializedPayload).asInstanceOf[classType.type]

    val serializedMetaData = new SimpleSerializedObject(
      (event \ "serializedMetaData").as[String],
      classOf[String],
      "ignored", "0")
    val metadata = s.deserialize(serializedMetaData).asInstanceOf[util.HashMap[String, _]]

    new SimpleDomainEventStream(new GenericDomainEventMessage[classType.type](`type`, timestamp, identifier, sequenceNumber, payload, metadata))
  }

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