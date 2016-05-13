package nl.ordina.eventstore

import scala.concurrent.ExecutionContext.Implicits.global

import org.axonframework.domain.{DomainEventMessage, DomainEventStream}
import org.axonframework.eventstore.EventStore
import org.axonframework.scynapse.serialization.xml.XStreamSerializer

import org.reactivecouchbase.CouchbaseRWImplicits.documentAsJsObjectReader
import org.reactivecouchbase.CouchbaseRWImplicits.jsObjectToDocumentWriter
import org.reactivecouchbase.ReactiveCouchbaseDriver

class CouchbaseEventStore extends EventStore {

  val driver = ReactiveCouchbaseDriver()
  val bucket = driver.bucket("default")

  val s = new XStreamSerializer()

  override def readEvents(`type`: String, identifier: scala.Any): DomainEventStream = ???

  override def appendEvents(`type`: String, events: DomainEventStream): Unit = {
    while (events.hasNext) {
      val next: DomainEventMessage[_] = events.next()
    }
  }

  def storeInCouchbase(value: DomainEventMessage[_]) = ???


  sys.ShutdownHookThread {
    driver.shutdown()
  }
}