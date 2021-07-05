package eu.mulk.mulkcms2.cms.privacy.about;

import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

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
