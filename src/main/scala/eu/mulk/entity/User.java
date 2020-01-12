package eu.mulk.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import java.util.Collection;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "users", schema = "public", catalog = "mulkcms")
public class User extends PanacheEntityBase {

  private int id;
  private String name;
  private String status;
  private String email;
  private String website;
  private Collection<ArticleRevision> articleRevisions;
  private Collection<CommentRevision> commentRevisions;
  private Collection<LoginCertificate> loginCertificates;
  private Collection<OpenId> openids;
  private Collection<Password> passwords;
  private Collection<UserPermission> userPermissions;
  private Collection<UserSetting> userSettings;

  @Id
  @Column(name = "id", nullable = false)
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  @Basic
  @Column(name = "name", nullable = true, length = -1)
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
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
  @Column(name = "email", nullable = true, length = -1)
  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  @Basic
  @Column(name = "website", nullable = true, length = -1)
  public String getWebsite() {
    return website;
  }

  public void setWebsite(String website) {
    this.website = website;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    User user = (User) o;
    return id == user.id &&
        Objects.equals(name, user.name) &&
        Objects.equals(status, user.status) &&
        Objects.equals(email, user.email) &&
        Objects.equals(website, user.website);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, status, email, website);
  }

  @OneToMany(mappedBy = "authors")
  public Collection<ArticleRevision> getArticleRevisions() {
    return articleRevisions;
  }

  public void setArticleRevisions(Collection<ArticleRevision> articleRevisions) {
    this.articleRevisions = articleRevisions;
  }

  @OneToMany(mappedBy = "user")
  public Collection<CommentRevision> getCommentRevisions() {
    return commentRevisions;
  }

  public void setCommentRevisions(Collection<CommentRevision> commentRevisions) {
    this.commentRevisions = commentRevisions;
  }

  @OneToMany(mappedBy = "user")
  public Collection<LoginCertificate> getLoginCertificates() {
    return loginCertificates;
  }

  public void setLoginCertificates(Collection<LoginCertificate> loginCertificates) {
    this.loginCertificates = loginCertificates;
  }

  @OneToMany(mappedBy = "user")
  public Collection<OpenId> getOpenids() {
    return openids;
  }

  public void setOpenids(Collection<OpenId> openids) {
    this.openids = openids;
  }

  @OneToMany(mappedBy = "user")
  public Collection<Password> getPasswords() {
    return passwords;
  }

  public void setPasswords(Collection<Password> passwords) {
    this.passwords = passwords;
  }

  @OneToMany(mappedBy = "user")
  public Collection<UserPermission> getUserPermissions() {
    return userPermissions;
  }

  public void setUserPermissions(Collection<UserPermission> userPermissions) {
    this.userPermissions = userPermissions;
  }

  @OneToMany(mappedBy = "user")
  public Collection<UserSetting> getUserSettings() {
    return userSettings;
  }

  public void setUserSettings(Collection<UserSetting> userSettings) {
    this.userSettings = userSettings;
  }
}
