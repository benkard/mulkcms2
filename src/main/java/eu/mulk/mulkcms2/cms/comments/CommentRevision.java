package eu.mulk.mulkcms2.cms.comments;

import eu.mulk.mulkcms2.cms.users.User;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.net.InetAddress;
import java.sql.Timestamp;
import javax.annotation.CheckForNull;

@Entity
@Table(name = "comment_revisions", schema = "public")
public class CommentRevision extends PanacheEntityBase {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  public Integer id;

  @Column(name = "date", nullable = true)
  @CheckForNull
  public Timestamp date;

  @Column(name = "content", nullable = false, length = -1)
  public String content;

  @Column(name = "format", nullable = false, length = -1)
  public String format;

  @Column(name = "status", nullable = false, length = -1)
  public String status;

  @Column(name = "article_revision", nullable = true)
  @CheckForNull
  public Integer articleRevision;

  @Column(name = "submitter_ip", nullable = true, columnDefinition = "inet")
  @CheckForNull
  public InetAddress submitterIp;

  @Column(name = "submitter_user_agent", nullable = true, length = -1)
  @CheckForNull
  public String submitterUserAgent;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "comment", referencedColumnName = "id", nullable = false)
  public Comment comment;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "author", referencedColumnName = "id")
  public User user;
}
