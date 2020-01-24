package eu.mulk.mulkcms2.cms.pages;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Id;

public class ArticleRevisionCharacteristicPK implements Serializable {
  private String characteristic;
  private int articleRevisionId;

  @Id
  public String getCharacteristic() {
    return characteristic;
  }

  public void setCharacteristic(String characteristic) {
    this.characteristic = characteristic;
  }

  @Id
  public int getArticleRevisionId() {
    return articleRevisionId;
  }

  public void setArticleRevisionId(int articleRevisionId) {
    this.articleRevisionId = articleRevisionId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof ArticleRevisionCharacteristicPK)) {
      return false;
    }
    ArticleRevisionCharacteristicPK that = (ArticleRevisionCharacteristicPK) o;
    return articleRevisionId == that.articleRevisionId
        && Objects.equals(characteristic, that.characteristic);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characteristic, articleRevisionId);
  }
}
