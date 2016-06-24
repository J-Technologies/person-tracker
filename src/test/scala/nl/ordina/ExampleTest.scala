/**
  * Copyright (C) 2016 Ordina
  */

package nl.ordina

import nl.ordina.commands.{CreateToDoItemCommand, MarkCompletedCommand}
import nl.ordina.events.{ToDoItemCompletedEvent, ToDoItemCreatedEvent}
import nl.ordina.example.ToDoItem
import org.axonframework.test.Fixtures
import org.scalatest.{FlatSpec, ShouldMatchers}

class ExampleTest extends FlatSpec with ShouldMatchers {
  "ToDoItem" should "be created" in new sut {
    fixture
      .given()
      .when(CreateToDoItemCommand("todo1", "need to implement the aggregate"))
      .expectEvents(ToDoItemCreatedEvent("todo1", "need to implement the aggregate"))
  }
  it should "be marked completed" in new sut {
    fixture
      .given(ToDoItemCreatedEvent("todo1", "need to implement the aggregate"))
      .when(MarkCompletedCommand("todo1"))
      .expectEvents(ToDoItemCompletedEvent("todo1"))
  }
}

trait sut {
  val fixture = {
    val f = Fixtures.newGivenWhenThenFixture(classOf[ToDoItem])
    f.setReportIllegalStateChange(false)
    f
  }
}
