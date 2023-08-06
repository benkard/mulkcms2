package eu.mulk.mulkcms2.benki.lazychat;

import eu.mulk.mulkcms2.benki.posts.PostText;
import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import javax.annotation.CheckForNull;

@Entity
@Table(name = "lazychat_message_texts", schema = "benki")
public class LazychatMessageText extends PostText<LazychatMessage> {

  @Column(name = "content", nullable = true, length = -1)
  @CheckForNull
  public String content;

  @CheckForNull
  @Override
  @JsonbTransient
  protected String getDescriptionMarkup() {
    return content;
  }
}
