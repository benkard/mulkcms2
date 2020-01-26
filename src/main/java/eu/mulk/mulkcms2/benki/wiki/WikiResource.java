package eu.mulk.mulkcms2.benki.wiki;

import static javax.ws.rs.core.MediaType.TEXT_HTML;

import io.quarkus.panache.common.Sort;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateExtension;
import io.quarkus.qute.TemplateInstance;
import io.quarkus.qute.api.ResourcePath;
import io.quarkus.security.Authenticated;
import io.quarkus.security.identity.SecurityIdentity;
import io.smallrye.jwt.auth.principal.JWTCallerPrincipal;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.TemporalAccessor;
import java.util.Optional;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import org.jboss.logging.Logger;
import org.jboss.resteasy.spi.NotImplementedYetException;

@Path("/wiki")
public class WikiResource {

  private static Logger log = Logger.getLogger(WikiResource.class);

  private static DateTimeFormatter htmlDateFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

  private static DateTimeFormatter humanDateFormatter =
      DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG, FormatStyle.SHORT);

  @ResourcePath("benki/wiki/wikiPage.html")
  @Inject
  Template wikiPage;

  @ResourcePath("benki/wiki/wikiPageRevisionList.html")
  @Inject
  Template wikiPageRevisionList;

  @Inject SecurityIdentity identity;

  @GET
  @Path("/{pageName}")
  @Produces(TEXT_HTML)
  public TemplateInstance getPage(@PathParam("pageName") String pageName) {
    Optional<WikiPageRevision> maybePage =
        WikiPageRevision.find(
                "from WikiPageRevision rev" + " join fetch rev.author" + " where rev.title = ?1",
                Sort.by("date").descending(),
                pageName)
            .firstResultOptional();
    if (maybePage.isEmpty()) {
      throw new NotFoundException();
    }
    var page = maybePage.get();
    return wikiPage.data("page", page);
  }

  @POST
  @Path("/{pageName}")
  @Authenticated
  public void updatePage(@PathParam("pageName") String pageName) {
    if (!identity.isAnonymous()) {
      var jwtCallerPrincipal = (JWTCallerPrincipal) identity.getPrincipal();
      log.infof("Logged in as user: %s", jwtCallerPrincipal.getName());
    }
    throw new NotImplementedYetException();
  }

  @GET
  @Path("/{pageName}/revisions")
  @Produces(TEXT_HTML)
  public TemplateInstance getPageRevisions(@PathParam("pageName") String pageName) {
    Optional<WikiPageRevision> maybePrimaryRevision =
        WikiPageRevision.find(
                "from WikiPageRevision rev" + " join fetch rev.author" + " where rev.title = ?1",
                Sort.by("date").descending(),
                pageName)
            .firstResultOptional();
    if (maybePrimaryRevision.isEmpty()) {
      throw new NotFoundException();
    }
    var primaryRevision = maybePrimaryRevision.get();

    WikiPage page =
        WikiPageRevision.find(
                "from WikiPage p"
                    + " join fetch p.revisions rev"
                    + " join fetch rev.author"
                    + " where p.id = ?1",
                primaryRevision.page.id)
            .singleResult();

    return wikiPageRevisionList.data("page", page).data("title", pageName);
  }

  @TemplateExtension
  static String humanDateTime(TemporalAccessor x) {
    return humanDateFormatter.format(x);
  }

  @TemplateExtension
  static String htmlDateTime(TemporalAccessor x) {
    return htmlDateFormatter.format(x);
  }
}
