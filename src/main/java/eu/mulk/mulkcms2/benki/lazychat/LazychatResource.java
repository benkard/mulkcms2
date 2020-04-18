package eu.mulk.mulkcms2.benki.lazychat;

import eu.mulk.mulkcms2.benki.posts.Post;
import eu.mulk.mulkcms2.benki.posts.PostFilter;
import eu.mulk.mulkcms2.benki.posts.PostResource;
import io.quarkus.security.Authenticated;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.OffsetDateTime;
import java.util.Objects;
import javax.transaction.Transactional;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.FormParam;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

@Path("/lazychat")
public class LazychatResource extends PostResource {

  public LazychatResource() {
    super(PostFilter.LAZYCHAT_MESSAGES_ONLY, "Lazy Chat");
  }

  @POST
  @Transactional
  @Authenticated
  public Response postMessage(
      @FormParam("text") String text, @FormParam("visibility") Post.Visibility visibility)
      throws URISyntaxException {

    var user = getCurrentUser();

    var message = new LazychatMessage();
    message.content = text;
    message.format = "markdown";
    message.owner = user;
    message.date = OffsetDateTime.now();

    assignPostTargets(visibility, user, message);

    message.persistAndFlush();

    return Response.seeOther(new URI("/lazychat")).build();
  }

  @POST
  @Transactional
  @Authenticated
  @Path("/p/{id}/edit")
  public Response patchMessage(
      @PathParam("id") int id,
      @FormParam("text") String text,
      @FormParam("visibility") Post.Visibility visibility)
      throws URISyntaxException {

    var user = getCurrentUser();

    var message = getSession().byId(LazychatMessage.class).load(id);

    if (message == null) {
      throw new NotFoundException();
    }

    if (!Objects.equals(message.owner.id, user.id)) {
      throw new ForbiddenException();
    }

    message.content = text;
    message.format = "markdown";

    assignPostTargets(visibility, user, message);

    return Response.seeOther(new URI("/lazychat")).build();
  }
}
