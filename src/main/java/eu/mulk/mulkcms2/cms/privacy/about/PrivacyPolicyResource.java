package eu.mulk.mulkcms2.cms.privacy.about;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import io.quarkus.qute.api.ResourcePath;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/privacy")
public class PrivacyPolicyResource {

  @ResourcePath("benki/privacy/index.html")
  Template index;

  @GET
  @Produces(MediaType.TEXT_HTML)
  public TemplateInstance getIndex() {
    return index.instance();
  }
}
