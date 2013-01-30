package api

import javax.ws.rs.{Consumes, POST, Path}
import javax.ws.rs.core.{MediaType, Response}
import java.net.URI

case class NewTaskRequest(description:String)

@Path("/tasks")
@Consumes(Array(MediaType.APPLICATION_JSON))
class TasksResource(tasksRepository:TasksRepository) {

  @POST
  def createTask(body:NewTaskRequest) = {
    val id = tasksRepository.create(body.description)
    Response.created(new URI("/%d".format(id))).build()
  }

}
