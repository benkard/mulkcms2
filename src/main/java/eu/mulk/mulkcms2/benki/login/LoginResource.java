package eu.mulk.mulkcms2.benki.login;

import io.quarkus.security.Authenticated;
import java.net.URI;
import java.net.URISyntaxException;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import org.jboss.logging.Logger;

@Path("/login")
public class LoginResource {

  private static final Logger log = Logger.getLogger(LoginResource.class);

  @GET
  @Authenticated
  public Response getRoot(@HeaderParam("referer") @DefaultValue("/posts") String referer)
      throws URISyntaxException {
    return Response.seeOther(new URI(referer)).build();
  }
}
