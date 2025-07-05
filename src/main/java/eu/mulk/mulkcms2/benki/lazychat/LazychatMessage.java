package eu.mulk.mulkcms2.benki.lazychat;

import eu.mulk.mulkcms2.benki.posts.Post;
import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.Collection;
import javax.annotation.CheckForNull;

@Entity
@Table(name = "lazychat_messages", schema = "benki")
public class LazychatMessage extends Post<LazychatMessageText> {

  @ManyToMany
  @JoinTable(
      name = "lazychat_references",
      schema = "benki",
      joinColumns = {@JoinColumn(name = "referrer")},
      inverseJoinColumns = {@JoinColumn(name = "referee")})
  @JsonbTransient
  public Collection<Post<?>> referees;

  @CheckForNull
  @Override
  public String getUri() {
    return null;
  }

  @CheckForNull
  @Override
  public String getVia() {
    return null;
  }

  @CheckForNull
  @Override
  public String getTitle() {
    return null;
  }

  @Override
  public boolean isBookmark() {
    return false;
  }

  @Override
  public boolean isLazychatMessage() {
    return true;
  }

  public void setContent(String x) {
    var text = getText();
    if (text == null) {
      text = new LazychatMessageText();
      text.post = this;
      text.postId = id;
      text.language = "";
      texts.put(text.language, text);
    }

    text.cachedDescriptionHtml = null;
    text.content = x;
  }
}
