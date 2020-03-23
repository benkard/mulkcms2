package eu.mulk.mulkcms2.benki.lazychat;

import static javax.ws.rs.core.MediaType.TEXT_HTML;

import eu.mulk.mulkcms2.benki.users.User;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateExtension;
import io.quarkus.qute.TemplateInstance;
import io.quarkus.qute.api.ResourcePath;
import io.quarkus.security.identity.SecurityIdentity;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.TemporalAccessor;
import javax.annotation.CheckForNull;
import javax.inject.Inject;
import javax.json.spi.JsonProvider;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.hibernate.Session;
import org.jboss.logging.Logger;

@Path("/lazychat")
public class LazychatResource {

  private static final Logger log = Logger.getLogger(LazychatResource.class);

  private static final DateTimeFormatter htmlDateFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

  private static final DateTimeFormatter humanDateFormatter =
      DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG, FormatStyle.SHORT);

  private static final JsonProvider jsonProvider = JsonProvider.provider();

  @ConfigProperty(name = "mulkcms.lazychat.default-max-results")
  int defaultMaxResults;

  @ResourcePath("benki/posts/postList.html")
  @Inject
  Template postList;

  @Inject SecurityIdentity identity;

  @PersistenceContext EntityManager entityManager;

  @GET
  @Produces(TEXT_HTML)
  public TemplateInstance getIndex(
      @QueryParam("i") @CheckForNull Integer cursor,
      @QueryParam("n") @CheckForNull Integer maxResults) {

    maxResults = maxResults == null ? defaultMaxResults : maxResults;

    var session = entityManager.unwrap(Session.class);
    var q = LazychatMessage.findViewable(session, identity, null, cursor, maxResults);

    return postList
        .data("posts", q.posts)
        .data("pageTitle", "Lazy Chat")
        .data("showBookmarkForm", false)
        .data("hasPreviousPage", q.prevCursor != null)
        .data("hasNextPage", q.nextCursor != null)
        .data("previousCursor", q.prevCursor)
        .data("nextCursor", q.nextCursor)
        .data("pageSize", maxResults);
  }

  @GET
  @Path("~{ownerName}")
  @Produces(TEXT_HTML)
  public TemplateInstance getUserIndex(
      @PathParam("ownerName") String ownerName,
      @QueryParam("i") @CheckForNull Integer cursor,
      @QueryParam("n") @CheckForNull Integer maxResults) {

    maxResults = maxResults == null ? defaultMaxResults : maxResults;

    var owner = User.findByNickname(ownerName);
    var session = entityManager.unwrap(Session.class);
    var q = LazychatMessage.findViewable(session, identity, owner, cursor, maxResults);

    return postList
        .data("posts", q.posts)
        .data("pageTitle", "Lazy Chat")
        .data("showBookmarkForm", false)
        .data("hasPreviousPage", q.prevCursor != null)
        .data("hasNextPage", q.nextCursor != null)
        .data("previousCursor", q.prevCursor)
        .data("nextCursor", q.nextCursor)
        .data("pageSize", maxResults);
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
