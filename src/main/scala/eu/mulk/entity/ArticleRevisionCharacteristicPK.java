package eu.mulk.entity;

import java.io.Serializable;
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
}
