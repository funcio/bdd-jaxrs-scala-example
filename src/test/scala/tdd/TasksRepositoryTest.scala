package tdd

import org.scalatest.FlatSpec
import api.TasksRepository

class TasksRepositoryTest extends FlatSpec {
  trait WithRepository{
    val tasksRepository = new TasksRepository
  }

  behavior of "TasksRepository"

  it should "create and retrieve a task" in {
    new WithRepository {
      val description = "Do something"
      val id:Int = tasksRepository.create(description)
      val task = tasksRepository.find(id).get
      assert(task.description === description)
    }
  }
}
