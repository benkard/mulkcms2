package eu.mulk.mulkcms2.cms.users;

import eu.mulk.mulkcms2.cms.comments.CommentRevision;
import eu.mulk.mulkcms2.cms.pages.ArticleRevision;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.Collection;
import javax.annotation.CheckForNull;

@Entity(name = "CmsUser")
@Table(name = "users", schema = "public")
public class User extends PanacheEntityBase {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  public Integer id;

  @Column(name = "name", nullable = true, length = -1)
  @CheckForNull
  public String name;

  @Column(name = "status", nullable = false, length = -1)
  public String status;

  @Column(name = "email", nullable = true, length = -1)
  @CheckForNull
  public String email;

  @Column(name = "website", nullable = true, length = -1)
  @CheckForNull
  public String website;

  @OneToMany(mappedBy = "authors", fetch = FetchType.LAZY)
  public Collection<ArticleRevision> articleRevisions;

  @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
  public Collection<CommentRevision> commentRevisions;

  @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
  public Collection<LoginCertificate> loginCertificates;

  @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
  public Collection<OpenId> openids;

  @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
  public Collection<Password> passwords;

  @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
  public Collection<UserPermission> userPermissions;

  @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
  public Collection<UserSetting> userSettings;
}
