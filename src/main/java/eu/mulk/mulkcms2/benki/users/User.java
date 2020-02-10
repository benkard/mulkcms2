package eu.mulk.mulkcms2.benki.users;

import eu.mulk.mulkcms2.benki.accesscontrol.PageKey;
import eu.mulk.mulkcms2.benki.accesscontrol.Role;
import eu.mulk.mulkcms2.benki.bookmarks.Bookmark;
import eu.mulk.mulkcms2.benki.generic.Post;
import eu.mulk.mulkcms2.benki.lazychat.LazychatMessage;
import eu.mulk.mulkcms2.benki.wiki.WikiPageRevision;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import java.util.Collection;
import java.util.Set;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity(name = "BenkiUser")
@Table(name = "users", schema = "benki")
public class User extends PanacheEntityBase {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  public Integer id;

  @Column(name = "first_name", nullable = true, length = -1)
  public String firstName;

  @Column(name = "middle_names", nullable = true, length = -1)
  public String middleNames;

  @Column(name = "last_name", nullable = true, length = -1)
  public String lastName;

  @Column(name = "email", nullable = true, length = -1)
  public String email;

  @Column(name = "website", nullable = true, length = -1)
  public String website;

  @Column(name = "status", nullable = true, length = -1)
  public String status;

  @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY)
  public Collection<Bookmark> bookmarks;

  @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY)
  public Collection<LazychatMessage> lazychatMessages;

  @ElementCollection(fetch = FetchType.LAZY)
  @CollectionTable(name = "openids", schema = "benki", joinColumns = @JoinColumn(name = "user"))
  @Column(name = "openid")
  public Set<String> openids;

  @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
  public Collection<PageKey> pageKeys;

  @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY)
  public Collection<Post> posts;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
      name = "user_default_target",
      schema = "benki",
      joinColumns = @JoinColumn(name = "user"),
      inverseJoinColumns = @JoinColumn(name = "target"))
  public Set<Role> defaultTargets;

  @ElementCollection(fetch = FetchType.LAZY)
  @CollectionTable(
      name = "user_email_addresses",
      schema = "benki",
      joinColumns = @JoinColumn(name = "user"))
  @Column(name = "email")
  public Set<String> emailAddresses;

  @ElementCollection(fetch = FetchType.LAZY)
  @CollectionTable(name = "user_jids", schema = "benki", joinColumns = @JoinColumn(name = "user"))
  @Column(name = "jid")
  public Set<String> jids;

  @ElementCollection(fetch = FetchType.LAZY)
  @CollectionTable(
      name = "user_nicknames",
      schema = "benki",
      joinColumns = @JoinColumn(name = "user"))
  @Column(name = "nickname")
  public Set<String> nicknames;

  @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
  public Collection<UserRole> directRoles;

  @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
  public Collection<RsaKey> rsaKeys;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "role", referencedColumnName = "id", nullable = false)
  public Role ownedRole;

  @ElementCollection(fetch = FetchType.LAZY)
  @CollectionTable(name = "webids", schema = "benki", joinColumns = @JoinColumn(name = "user"))
  @Column(name = "webid")
  public Set<String> webids;

  @OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
  public Collection<WikiPageRevision> wikiPageRevisions;

  @ManyToMany(mappedBy = "visibleTo", fetch = FetchType.LAZY)
  public Set<Post> visiblePosts;

  @ManyToMany(mappedBy = "visibleTo", fetch = FetchType.LAZY)
  public Set<Bookmark> visibleBookmarks;

  @ManyToMany(mappedBy = "visibleTo", fetch = FetchType.LAZY)
  public Set<LazychatMessage> visibleLazychatMessages;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
      name = "effective_user_roles",
      schema = "benki",
      joinColumns = @JoinColumn(name = "user"),
      inverseJoinColumns = @JoinColumn(name = "role"))
  public Set<Role> effectiveRoles;
}
