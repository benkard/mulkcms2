package eu.mulk.mulkcms2.benki.lazychat;

import static javax.ws.rs.core.MediaType.APPLICATION_FORM_URLENCODED;
import static javax.ws.rs.core.MediaType.MULTIPART_FORM_DATA;
import static javax.ws.rs.core.MediaType.WILDCARD;

import eu.mulk.mulkcms2.benki.posts.Post;
import eu.mulk.mulkcms2.benki.posts.PostFilter;
import eu.mulk.mulkcms2.benki.posts.PostResource;
import io.quarkus.security.Authenticated;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;
import java.time.OffsetDateTime;
import java.util.Objects;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.FormParam;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("/lazychat")
public class LazychatResource extends PostResource {

  public LazychatResource() throws NoSuchAlgorithmException {
    super(PostFilter.LAZYCHAT_MESSAGES_ONLY, "Lazy Chat");
  }

  @POST
  @Transactional
  @Authenticated
  @Produces(WILDCARD)
  @Consumes({APPLICATION_FORM_URLENCODED, MULTIPART_FORM_DATA})
  public Response postMessage(
      @FormParam("text") @NotNull String text,
      @FormParam("visibility") @NotNull Post.Visibility visibility)
      throws URISyntaxException {

    var user = Objects.requireNonNull(getCurrentUser());

    var message = new LazychatMessage();
    message.setContent(text);
    message.owner = user;
    message.date = OffsetDateTime.now();

    assignPostTargets(visibility, user, message);

    message.persistAndFlush();

    return Response.seeOther(new URI("/lazychat")).build();
  }

  @POST
  @Transactional
  @Authenticated
  @Produces(WILDCARD)
  @Consumes({APPLICATION_FORM_URLENCODED, MULTIPART_FORM_DATA})
  @Path("{id}/edit")
  public Response patchMessage(
      @PathParam("id") int id,
      @FormParam("text") String text,
      @FormParam("visibility") Post.Visibility visibility)
      throws URISyntaxException {

    var user = Objects.requireNonNull(getCurrentUser());

    var message = getSession().byId(LazychatMessage.class).load(id);

    if (message == null) {
      throw new NotFoundException();
    }

    if (message.owner == null || !Objects.equals(message.owner.id, user.id)) {
      throw new ForbiddenException();
    }

    message.setContent(text);

    assignPostTargets(visibility, user, message);

    return Response.seeOther(new URI("/lazychat")).build();
  }
}
