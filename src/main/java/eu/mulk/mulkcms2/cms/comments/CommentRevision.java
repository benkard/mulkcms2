package eu.mulk.mulkcms2.cms.comments;

import com.vladmihalcea.hibernate.type.basic.Inet;
import com.vladmihalcea.hibernate.type.basic.PostgreSQLInetType;
import eu.mulk.mulkcms2.cms.users.User;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.TypeDef;

@Entity
@Table(name = "comment_revisions", schema = "public")
@TypeDef(name = "inet", typeClass = PostgreSQLInetType.class, defaultForType = Inet.class)
public class CommentRevision extends PanacheEntityBase {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  public Integer id;

  @Column(name = "date", nullable = true)
  public Timestamp date;

  @Column(name = "content", nullable = false, length = -1)
  public String content;

  @Column(name = "format", nullable = false, length = -1)
  public String format;

  @Column(name = "status", nullable = false, length = -1)
  public String status;

  @Column(name = "article_revision", nullable = true)
  public Integer articleRevision;

  @Column(name = "submitter_ip", nullable = true, columnDefinition = "inet")
  public Inet submitterIp;

  @Column(name = "submitter_user_agent", nullable = true, length = -1)
  public String submitterUserAgent;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "comment", referencedColumnName = "id", nullable = false)
  public Comment comment;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "author", referencedColumnName = "id")
  public User user;
}
