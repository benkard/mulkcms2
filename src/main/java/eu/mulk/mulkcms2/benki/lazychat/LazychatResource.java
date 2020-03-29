package eu.mulk.mulkcms2.benki.lazychat;

import eu.mulk.mulkcms2.benki.accesscontrol.Role;
import eu.mulk.mulkcms2.benki.posts.PostFilter;
import eu.mulk.mulkcms2.benki.posts.PostResource;
import eu.mulk.mulkcms2.benki.users.User;
import io.quarkus.security.Authenticated;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.OffsetDateTime;
import java.util.Set;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
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
      @FormParam("text") String text,
      @FormParam("visibility") @NotNull @Pattern(regexp = "public|semiprivate|private")
          String visibility)
      throws URISyntaxException {

    var userName = identity.getPrincipal().getName();
    var user = User.findByNickname(userName);

    var message = new LazychatMessage();
    message.content = text;
    message.format = "markdown";
    message.owner = user;
    message.date = OffsetDateTime.now();

    if (visibility.equals("public")) {
      Role world = Role.find("from Role r join r.tags tag where tag = 'world'").singleResult();
      message.targets = Set.of(world);
    } else if (visibility.equals("semiprivate")) {
      message.targets = Set.copyOf(user.defaultTargets);
    } else if (!visibility.equals("private")) {
      throw new BadRequestException(String.format("invalid visibility “%s”", visibility));
    }

    message.persistAndFlush();

    return Response.seeOther(new URI("/lazychat")).build();
  }
}
