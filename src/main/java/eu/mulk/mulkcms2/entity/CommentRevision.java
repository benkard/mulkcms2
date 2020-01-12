package eu.mulk.mulkcms2.entity;

import com.vladmihalcea.hibernate.type.basic.Inet;
import com.vladmihalcea.hibernate.type.basic.PostgreSQLInetType;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import java.sql.Timestamp;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.TypeDef;

@Entity
@Table(name = "comment_revisions", schema = "public", catalog = "mulkcms")
@TypeDef(
    name = "inet",
    typeClass = PostgreSQLInetType.class,
    defaultForType = Inet.class
)
public class CommentRevision extends PanacheEntityBase {

  private int id;
  private Timestamp date;
  private String content;
  private String format;
  private String status;
  private Integer articleRevision;
  private Inet submitterIp;
  private String submitterUserAgent;
  private Comment comment;
  private User user;

  @Id
  @Column(name = "id", nullable = false)
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  @Basic
  @Column(name = "date", nullable = true)
  public Timestamp getDate() {
    return date;
  }

  public void setDate(Timestamp date) {
    this.date = date;
  }

  @Basic
  @Column(name = "content", nullable = false, length = -1)
  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  @Basic
  @Column(name = "format", nullable = false, length = -1)
  public String getFormat() {
    return format;
  }

  public void setFormat(String format) {
    this.format = format;
  }

  @Basic
  @Column(name = "status", nullable = false, length = -1)
  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  @Basic
  @Column(name = "article_revision", nullable = true)
  public Integer getArticleRevision() {
    return articleRevision;
  }

  public void setArticleRevision(Integer articleRevision) {
    this.articleRevision = articleRevision;
  }

  @Column(name = "submitter_ip", nullable = true, columnDefinition = "inet")
  public Inet getSubmitterIp() {
    return submitterIp;
  }

  public void setSubmitterIp(Inet submitterIp) {
    this.submitterIp = submitterIp;
  }

  @Basic
  @Column(name = "submitter_user_agent", nullable = true, length = -1)
  public String getSubmitterUserAgent() {
    return submitterUserAgent;
  }

  public void setSubmitterUserAgent(String submitterUserAgent) {
    this.submitterUserAgent = submitterUserAgent;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CommentRevision that = (CommentRevision) o;
    return id == that.id &&
        Objects.equals(date, that.date) &&
        Objects.equals(content, that.content) &&
        Objects.equals(format, that.format) &&
        Objects.equals(status, that.status) &&
        Objects.equals(articleRevision, that.articleRevision) &&
        Objects.equals(submitterIp, that.submitterIp) &&
        Objects.equals(submitterUserAgent, that.submitterUserAgent);
  }

  @Override
  public int hashCode() {
    return Objects
        .hash(id, date, content, format, status, articleRevision, submitterIp, submitterUserAgent);
  }

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "comment", referencedColumnName = "id", nullable = false)
  public Comment getComment() {
    return comment;
  }

  public void setComment(Comment comment) {
    this.comment = comment;
  }

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "author", referencedColumnName = "id")
  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }
}
