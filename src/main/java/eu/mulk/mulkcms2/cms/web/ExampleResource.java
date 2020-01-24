package eu.mulk.mulkcms2.cms.web;

import io.quarkus.security.Authenticated;
import io.quarkus.security.identity.SecurityIdentity;
import io.smallrye.jwt.auth.principal.JWTCallerPrincipal;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.jboss.logging.Logger;

@Path("/example")
public class ExampleResource {

  private static Logger log = Logger.getLogger(ExampleResource.class);

  @Inject
  SecurityIdentity identity;

  @GET
  @Produces({MediaType.TEXT_PLAIN})
  @Authenticated
  public String hello() {
    if (!identity.isAnonymous()) {
      var jwtCallerPrincipal = (JWTCallerPrincipal) identity.getPrincipal();
      log.infof("Logged in as user: %s", jwtCallerPrincipal.getName());
    }
    return "hello!";
  }
}
