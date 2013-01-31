package api

import collection.mutable

case class Task(id:Int, description:String, done:Boolean)

class TasksRepository {

  val tasks = mutable.HashMap[Int,Task]()
  var lastId = 0

  def create(description:String) = {
    this.lastId += 1
    tasks += lastId -> Task(lastId,description, done=false)
    lastId
  }

  def find(id: Int): Option[Task] = tasks.get(id);

}
