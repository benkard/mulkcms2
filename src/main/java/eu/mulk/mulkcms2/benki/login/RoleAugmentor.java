package eu.mulk.mulkcms2.benki.login;

import eu.mulk.mulkcms2.benki.accesscontrol.Role;
import eu.mulk.mulkcms2.benki.users.User;
import io.quarkus.cache.CacheResult;
import io.quarkus.security.identity.AuthenticationRequestContext;
import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.security.identity.SecurityIdentityAugmentor;
import io.quarkus.security.runtime.QuarkusSecurityIdentity;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ApplicationScoped
public class RoleAugmentor implements SecurityIdentityAugmentor {

  private static final String EDITOR_TAG = "editor";

  @Override
  public Uni<SecurityIdentity> augment(
      SecurityIdentity identity, AuthenticationRequestContext context) {

    if (identity.isAnonymous()) {
      return Uni.createFrom().item(identity);
    }

    return augmentWithRoles(identity, context);
  }

  Uni<SecurityIdentity> augmentWithRoles(
      SecurityIdentity identity, AuthenticationRequestContext context) {
    return context.runBlocking(
        () -> {
          Set<String> loginRoles = getUserLoginRoles(identity.getPrincipal().getName());
          return QuarkusSecurityIdentity.builder(identity).addRoles(loginRoles).build();
        });
  }

  @CacheResult(cacheName = "login-role-cache")
  @Transactional
  Set<String> getUserLoginRoles(String userNickname) {
    var user = User.findByNicknameWithRoles(userNickname);
    return user.effectiveRoles.stream()
        .flatMap(RoleAugmentor::roleTags)
        .flatMap(RoleAugmentor::loginRoleOfTag)
        .collect(Collectors.toSet());
  }

  private static Stream<String> roleTags(Role role) {
    return role.tags.stream();
  }

  private static Stream<String> loginRoleOfTag(String tag) {
    return tag.equals(EDITOR_TAG) ? Stream.of(LoginRoles.EDITOR) : Stream.empty();
  }
}
