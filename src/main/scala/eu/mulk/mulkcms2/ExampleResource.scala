package eu.mulk.mulkcms2

import javax.ws.rs.core.MediaType
import javax.ws.rs.{GET, Path, Produces}

@Path("/hello")
class ExampleResource {

  @GET
  @Produces(Array[String](MediaType.TEXT_PLAIN))
  def hello() = "hello!"
}
