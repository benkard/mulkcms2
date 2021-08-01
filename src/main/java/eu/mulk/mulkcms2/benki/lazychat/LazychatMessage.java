package eu.mulk.mulkcms2.benki.lazychat;

import eu.mulk.mulkcms2.benki.posts.Post;
import java.util.Collection;
import javax.annotation.CheckForNull;
import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

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
  public Collection<LazychatMessage> referees;

  @ManyToMany(mappedBy = "referees")
  @JsonbTransient
  public Collection<LazychatMessage> referrers;

  @CheckForNull
  @Override
  public String getUri() {
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
      text.language = "";
      texts.put(text.language, text);
    }

    text.cachedDescriptionHtml = null;
    text.content = x;
  }
}
