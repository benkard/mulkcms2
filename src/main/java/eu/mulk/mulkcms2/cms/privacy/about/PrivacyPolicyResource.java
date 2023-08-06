package eu.mulk.mulkcms2.cms.privacy.about;

import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/privacy")
public class PrivacyPolicyResource {

  @CheckedTemplate(basePath = "benki/privacy")
  static class Templates {
    public static native TemplateInstance index();
  }

  @GET
  @Produces(MediaType.TEXT_HTML)
  public TemplateInstance getIndex() {
    return Templates.index();
  }
}
