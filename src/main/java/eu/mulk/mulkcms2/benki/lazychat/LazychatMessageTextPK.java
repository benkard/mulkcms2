package eu.mulk.mulkcms2.benki.lazychat;

import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.io.Serializable;
import java.util.Objects;

public class LazychatMessageTextPK implements Serializable {

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "lazychat_message", referencedColumnName = "id", nullable = false)
  public LazychatMessage lazychatMessage;

  @Id
  @Column(name = "language", nullable = false, length = -1)
  private String language;

  public LazychatMessage getLazychatMessage() {
    return lazychatMessage;
  }

  public void setLazychatMessageId(LazychatMessage lazychatMessage) {
    this.lazychatMessage = lazychatMessage;
  }

  public String getLanguage() {
    return language;
  }

  public void setLanguage(String language) {
    this.language = language;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof LazychatMessageTextPK)) {
      return false;
    }
    LazychatMessageTextPK that = (LazychatMessageTextPK) o;
    return Objects.equals(getLazychatMessage(), that.getLazychatMessage())
        && getLanguage().equals(that.getLanguage());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getLazychatMessage(), getLanguage());
  }
}
