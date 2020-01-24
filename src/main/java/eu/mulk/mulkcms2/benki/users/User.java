package eu.mulk.mulkcms2.benki.users;

import eu.mulk.mulkcms2.benki.accesscontrol.PageKey;
import eu.mulk.mulkcms2.benki.accesscontrol.Role;
import eu.mulk.mulkcms2.benki.bookmarx.Bookmark;
import eu.mulk.mulkcms2.benki.generic.Post;
import eu.mulk.mulkcms2.benki.lafargue.LazychatMessage;
import eu.mulk.mulkcms2.benki.wiki.WikiPageRevision;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import java.util.Collection;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity(name = "BenkiUser")
@Table(name = "users", schema = "public", catalog = "benki")
public class User extends PanacheEntityBase {

  @Id
  @Column(name = "id", nullable = false)
  public int id;

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

  @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
  public Collection<Openids> openids;

  @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
  public Collection<PageKey> pageKeys;

  @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY)
  public Collection<Post> posts;

  @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
  public Collection<UserDefaultTarget> defaultTargets;

  @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
  public Collection<UserEmailAddress> emailAddresses;

  @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
  public Collection<UserJid> jids;

  @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
  public Collection<UserNickname> nicknames;

  @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
  public Collection<UserRole> directRoles;

  @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
  public Collection<UserRsaKey> rsaKeys;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "role", referencedColumnName = "id", nullable = false)
  public Role ownedRole;

  @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
  public Collection<WebId> webids;

  @OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
  public Collection<WikiPageRevision> wikiPageRevisions;

  @ManyToMany(mappedBy = "visibleTo", fetch = FetchType.LAZY)
  public Collection<Post> visiblePosts;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
      name = "effective_user_roles",
      joinColumns = @JoinColumn(name = "user"),
      inverseJoinColumns = @JoinColumn(name = "role"))
  public Set<Role> effectiveRoles;
}
