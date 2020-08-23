package eu.mulk.mulkcms2.benki.lazychat;

import eu.mulk.mulkcms2.benki.posts.PostText;
import eu.mulk.mulkcms2.common.markdown.MarkdownConverter;
import javax.annotation.CheckForNull;
import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "lazychat_message_texts", schema = "benki")
public class LazychatMessageText extends PostText<LazychatMessage> {

  @Column(name = "content", nullable = true, length = -1)
  @CheckForNull
  public String content;

  @CheckForNull
  @Override
  @JsonbTransient
  protected String computeDescriptionHtml() {
    if (content == null) {
      return null;
    }
    return new MarkdownConverter().htmlify(content);
  }
}
