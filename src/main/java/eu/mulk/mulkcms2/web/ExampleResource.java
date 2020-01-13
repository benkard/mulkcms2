package eu.mulk.mulkcms2.web;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

public class ExampleResource {

  @GET
  @Produces({MediaType.TEXT_PLAIN})
  public String hello() {
    return "hello!";
  }
}
