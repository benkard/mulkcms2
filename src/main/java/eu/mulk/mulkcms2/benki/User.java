package eu.mulk.mulkcms2.benki;

import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "users", schema = "public", catalog = "benki")
public class User {

  private int id;
  private String firstName;
  private String middleNames;
  private String lastName;
  private String email;
  private String website;
  private String status;
  private Collection<Bookmark> bookmarks;
  private Collection<LazychatMessage> lazychatMessages;
  private Collection<Openids> openids;
  private Collection<PageKey> pageKeys;
  private Collection<Post> posts;
  private Collection<UserDefaultTarget> defaultTargets;
  private Collection<UserEmailAddress> emailAddresses;
  private Collection<UserJid> jids;
  private Collection<UserNickname> nicknames;
  private Collection<UserRole> roles;
  private Collection<UserRsaKey> rsaKeys;
  private Role ownedRole;
  private Collection<WebId> webids;
  private Collection<WikiPageRevision> wikiPageRevisions;

  @Id
  @Column(name = "id", nullable = false)
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  @Basic
  @Column(name = "first_name", nullable = true, length = -1)
  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  @Basic
  @Column(name = "middle_names", nullable = true, length = -1)
  public String getMiddleNames() {
    return middleNames;
  }

  public void setMiddleNames(String middleNames) {
    this.middleNames = middleNames;
  }

  @Basic
  @Column(name = "last_name", nullable = true, length = -1)
  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
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

  @Basic
  @Column(name = "status", nullable = true, length = -1)
  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
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

    if (id != user.id) {
      return false;
    }
    if (firstName != null ? !firstName.equals(user.firstName) : user.firstName != null) {
      return false;
    }
    if (middleNames != null ? !middleNames.equals(user.middleNames) : user.middleNames != null) {
      return false;
    }
    if (lastName != null ? !lastName.equals(user.lastName) : user.lastName != null) {
      return false;
    }
    if (email != null ? !email.equals(user.email) : user.email != null) {
      return false;
    }
    if (website != null ? !website.equals(user.website) : user.website != null) {
      return false;
    }
    if (status != null ? !status.equals(user.status) : user.status != null) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = id;
    result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
    result = 31 * result + (middleNames != null ? middleNames.hashCode() : 0);
    result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
    result = 31 * result + (email != null ? email.hashCode() : 0);
    result = 31 * result + (website != null ? website.hashCode() : 0);
    result = 31 * result + (status != null ? status.hashCode() : 0);
    return result;
  }

  @OneToMany(mappedBy = "owner")
  public Collection<Bookmark> getBookmarks() {
    return bookmarks;
  }

  public void setBookmarks(Collection<Bookmark> bookmarks) {
    this.bookmarks = bookmarks;
  }

  @OneToMany(mappedBy = "owner")
  public Collection<LazychatMessage> getLazychatMessages() {
    return lazychatMessages;
  }

  public void setLazychatMessages(
      Collection<LazychatMessage> lazychatMessages) {
    this.lazychatMessages = lazychatMessages;
  }

  @OneToMany(mappedBy = "user")
  public Collection<Openids> getOpenids() {
    return openids;
  }

  public void setOpenids(Collection<Openids> openids) {
    this.openids = openids;
  }

  @OneToMany(mappedBy = "user")
  public Collection<PageKey> getPageKeys() {
    return pageKeys;
  }

  public void setPageKeys(Collection<PageKey> pageKeys) {
    this.pageKeys = pageKeys;
  }

  @OneToMany(mappedBy = "owner")
  public Collection<Post> getPosts() {
    return posts;
  }

  public void setPosts(Collection<Post> posts) {
    this.posts = posts;
  }

  @OneToMany(mappedBy = "user")
  public Collection<UserDefaultTarget> getDefaultTargets() {
    return defaultTargets;
  }

  public void setDefaultTargets(
      Collection<UserDefaultTarget> defaultTargets) {
    this.defaultTargets = defaultTargets;
  }

  @OneToMany(mappedBy = "user")
  public Collection<UserEmailAddress> getEmailAddresses() {
    return emailAddresses;
  }

  public void setEmailAddresses(
      Collection<UserEmailAddress> emailAddresses) {
    this.emailAddresses = emailAddresses;
  }

  @OneToMany(mappedBy = "user")
  public Collection<UserJid> getJids() {
    return jids;
  }

  public void setJids(Collection<UserJid> jids) {
    this.jids = jids;
  }

  @OneToMany(mappedBy = "user")
  public Collection<UserNickname> getNicknames() {
    return nicknames;
  }

  public void setNicknames(Collection<UserNickname> nicknames) {
    this.nicknames = nicknames;
  }

  @OneToMany(mappedBy = "user")
  public Collection<UserRole> getRoles() {
    return roles;
  }

  public void setRoles(Collection<UserRole> roles) {
    this.roles = roles;
  }

  @OneToMany(mappedBy = "user")
  public Collection<UserRsaKey> getRsaKeys() {
    return rsaKeys;
  }

  public void setRsaKeys(Collection<UserRsaKey> rsaKeys) {
    this.rsaKeys = rsaKeys;
  }

  @ManyToOne
  @JoinColumn(name = "role", referencedColumnName = "id", nullable = false)
  public Role getOwnedRole() {
    return ownedRole;
  }

  public void setOwnedRole(Role ownedRole) {
    this.ownedRole = ownedRole;
  }

  @OneToMany(mappedBy = "user")
  public Collection<WebId> getWebids() {
    return webids;
  }

  public void setWebids(Collection<WebId> webids) {
    this.webids = webids;
  }

  @OneToMany(mappedBy = "author")
  public Collection<WikiPageRevision> getWikiPageRevisions() {
    return wikiPageRevisions;
  }

  public void setWikiPageRevisions(
      Collection<WikiPageRevision> wikiPageRevisions) {
    this.wikiPageRevisions = wikiPageRevisions;
  }
}
