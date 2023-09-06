package eu.mulk.mulkcms2.benki.posts;

import io.smallrye.common.annotation.Blocking;
import jakarta.ws.rs.Path;
import java.security.NoSuchAlgorithmException;

@Path("/posts")
@Blocking
public class AllPostsResource extends PostResource {

  public AllPostsResource() throws NoSuchAlgorithmException {
    super(PostFilter.ALL, "All Posts");
  }
}
