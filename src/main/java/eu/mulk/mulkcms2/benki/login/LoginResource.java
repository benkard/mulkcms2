package eu.mulk.mulkcms2.benki.login;

import io.quarkus.security.Authenticated;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import java.net.URI;
import java.net.URISyntaxException;

@Path("/login")
public class LoginResource {

  @GET
  @Authenticated
  public Response getRoot(@HeaderParam("referer") @DefaultValue("/posts") String referer)
      throws URISyntaxException {
    return Response.seeOther(new URI(referer)).build();
  }
}
