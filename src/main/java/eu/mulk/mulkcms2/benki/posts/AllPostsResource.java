package eu.mulk.mulkcms2.benki.posts;

import java.security.NoSuchAlgorithmException;
import javax.ws.rs.Path;

@Path("/posts")
public class AllPostsResource extends PostResource {

  public AllPostsResource() throws NoSuchAlgorithmException {
    super(PostFilter.ALL, "All Posts");
  }
}
