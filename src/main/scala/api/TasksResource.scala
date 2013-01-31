package api

import javax.ws.rs._
import javax.ws.rs.core.{MediaType, Response}
import java.net.URI
import javax.ws.rs.Path

case class NewTaskRequest(description:String)

@Path("/tasks")
@Consumes(Array(MediaType.APPLICATION_JSON))
@Produces(Array(MediaType.APPLICATION_JSON))
class TasksResource(tasksRepository:TasksRepository) {

  @POST
  def createTask(body:NewTaskRequest) = {
    val id = tasksRepository.create(body.description)
    Response.created(new URI("/%d".format(id))).build()
  }

  @GET
  @Path("/{id}")
  def findTask(@PathParam("id") id:Integer): Task = tasksRepository.find(id).get
}
