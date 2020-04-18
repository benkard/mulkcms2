package eu.mulk.mulkcms2.benki.posts;

import javax.ws.rs.Path;

@Path("/posts")
public class AllPostsResource extends PostResource {

  public AllPostsResource() {
    super(PostFilter.ALL, "All Posts");
  }
}
