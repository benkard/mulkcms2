package eu.mulk.mulkcms2.benki.wiki;

import static javax.ws.rs.core.MediaType.TEXT_HTML;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import io.quarkus.qute.api.ResourcePath;
import javax.inject.Inject;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

@Path("/wiki")
public class WikiResource {

  @ResourcePath("benki/wiki/wikiPage.html")
  @Inject Template wikiPage;

  @GET
  @Path("/pages/{pageName}")
  @Produces(TEXT_HTML)
  public TemplateInstance getPage(@PathParam("pageName") String pageName) {
    return wikiPage.data("title", "TEST");
  }
}
