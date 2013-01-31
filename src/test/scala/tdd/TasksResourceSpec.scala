package tdd

import org.mockito.Mockito.{verify, when}
import org.scalatest.FlatSpec
import api.{Task, NewTaskRequest, TasksResource, TasksRepository}
import org.scalatest.mock.MockitoSugar
import java.net.URI

class TasksResourceSpec extends FlatSpec with MockitoSugar{

  trait WithMocks{
    val tasksRepository = mock[TasksRepository]
    val tasksResource = new TasksResource(tasksRepository)
  }

  behavior of "TasksResource"

  it should "create a new task" in {
    new WithMocks {
      val description = "Write a Bdd example in Scala"
      when(tasksRepository.create(description)).thenReturn(42)
      val response = tasksResource.createTask(NewTaskRequest(description))
      assert(response.getStatus === 201)
      assert(response.getMetadata.getFirst("Location") === new URI("/42"))
    }
  }

  it should "find a task" in {
    new WithMocks {
      val task = Task(1,"Write a Bdd example in Scala", false)
      val id = 1;
      when(tasksRepository.find(id)).thenReturn(Some(task))
      val result = tasksResource.findTask(id)
      assert(result === task)
    }
  }
}
