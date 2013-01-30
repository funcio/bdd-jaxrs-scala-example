package server

import com.fasterxml.jackson.databind.{SerializationFeature, ObjectMapper}
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import util.Properties
import javax.ws.rs.ext.{ContextResolver, Provider}
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType
import api.{TasksRepository, TasksResource}

object Context{

  private def intEnvOrElse(propertyName:String, defaultValue:Int) = Properties.envOrElse(propertyName, defaultValue.toString).toInt

  val webServerPort = intEnvOrElse("PORT",8080)

  val server = new Server(webServerPort)

  val mapper = {
    val mapper = new ObjectMapper()
    mapper.registerModule(DefaultScalaModule)
    mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)
    mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS,false)
    mapper.configure(SerializationFeature.INDENT_OUTPUT,true)
    mapper
  }

  val tasksResource = new TasksResource(new TasksRepository)

  val instances:Set[AnyRef] = Set[AnyRef](
    tasksResource,
    new JacksonContextResolver(mapper))

}

@Provider
@Produces(Array(MediaType.APPLICATION_JSON))
class JacksonContextResolver(mapper:ObjectMapper) extends ContextResolver[ObjectMapper]{

  def getContext(clazz: Class[_]) = mapper
}