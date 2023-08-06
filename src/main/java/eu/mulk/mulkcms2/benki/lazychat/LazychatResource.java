package eu.mulk.mulkcms2.benki.lazychat;

import static jakarta.ws.rs.core.MediaType.APPLICATION_FORM_URLENCODED;
import static jakarta.ws.rs.core.MediaType.MULTIPART_FORM_DATA;
import static jakarta.ws.rs.core.MediaType.WILDCARD;

import eu.mulk.mulkcms2.benki.posts.Post;
import eu.mulk.mulkcms2.benki.posts.PostFilter;
import eu.mulk.mulkcms2.benki.posts.PostResource;
import io.quarkus.security.Authenticated;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;
import java.time.OffsetDateTime;
import java.util.Objects;

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
