package eu.mulk.mulkcms2.cms.about;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import io.quarkus.qute.api.ResourcePath;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/about")
class AboutResource {

  @ResourcePath("benki/about/index.html")
  var index: Template = _

  @GET
  @Produces(Array(MediaType.TEXT_HTML))
  def getIndex: TemplateInstance =
    index.instance()
}
