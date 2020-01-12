package eu.mulk.mulkcms2.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import java.util.Collection;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "users", schema = "public", catalog = "mulkcms")
public class User extends PanacheEntityBase {

  @Id
  @Column(name = "id", nullable = false)
  public int id;

  @Column(name = "name", nullable = true, length = -1)
  public String name;

  @Column(name = "status", nullable = false, length = -1)
  public String status;

  @Column(name = "email", nullable = true, length = -1)
  public String email;

  @Column(name = "website", nullable = true, length = -1)
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
