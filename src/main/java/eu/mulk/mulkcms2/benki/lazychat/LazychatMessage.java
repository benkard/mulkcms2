package eu.mulk.mulkcms2.benki.lazychat;

import eu.mulk.mulkcms2.benki.generic.Post;
import eu.mulk.mulkcms2.benki.users.User;
import eu.mulk.mulkcms2.common.markdown.MarkdownConverter;
import io.quarkus.security.identity.SecurityIdentity;
import java.util.Collection;
import java.util.List;
import javax.annotation.CheckForNull;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.Session;

@Entity
@Table(name = "lazychat_messages", schema = "benki")
public class LazychatMessage extends Post {

  @Column(name = "content", nullable = true, length = -1)
  public String content;

  @Column(name = "format", nullable = false, length = -1)
  public String format;

  @OneToMany(mappedBy = "referrer", fetch = FetchType.LAZY)
  public Collection<LazychatReference> references;

  @Transient
  public String getContentHtml() {
    return new MarkdownConverter().htmlify(content);
  }

  public static List<LazychatMessage> findViewable(
      Session session, SecurityIdentity viewer, @CheckForNull User owner) {
    return findViewable(LazychatMessage.class, session, viewer, owner, null, null).posts;
  }

  public static PostPage<LazychatMessage> findViewable(
      Session session,
      SecurityIdentity viewer,
      @CheckForNull User owner,
      @CheckForNull Integer cursor,
      @CheckForNull Integer count) {
    return findViewable(LazychatMessage.class, session, viewer, owner, cursor, count);
  }
}
