package eu.mulk.mulkcms2.cms.about;

import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/about")
public class AboutResource {

  @CheckedTemplate(basePath = "benki/about")
  static class Templates {
    public static native TemplateInstance index();
  }

  @GET
  @Produces(MediaType.TEXT_HTML)
  public TemplateInstance getIndex() {
    return Templates.index();
  }
}
