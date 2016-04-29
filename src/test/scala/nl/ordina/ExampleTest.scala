package nl.ordina

import nl.ordina.commands.{CreateToDoItemCommand, MarkCompletedCommand}
import nl.ordina.events.{ToDoItemCompletedEvent, ToDoItemCreatedEvent}
import nl.ordina.example.ToDoItem
import org.axonframework.scynapse.test.EventMatchers
import org.axonframework.test.Fixtures
import org.scalatest.{FlatSpec, ShouldMatchers}


class ExampleTest extends FlatSpec with ShouldMatchers with EventMatchers {
  "ToDoItem" should "be created" in new sut {
    fixture
      .given()
      .when(CreateToDoItemCommand("todo1", "need to implement the aggregate"))
      .expectEventsMatching( withPayloads(isEqualTo(ToDoItemCreatedEvent("todo1", "need to implement the aggregate"))))
  }
  it should "be marked completed" in new sut {
    fixture
      .given(ToDoItemCreatedEvent("todo1", "need to implement the aggregate"))
      .when(MarkCompletedCommand("todo1"))
      .expectEventsMatching( withPayloads(isEqualTo(ToDoItemCompletedEvent("todo1"))))
  }
}

trait sut {
  val fixture = {
    val f = Fixtures.newGivenWhenThenFixture(classOf[ToDoItem])
    f.setReportIllegalStateChange(false)
    f
  }
}
