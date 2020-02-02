package eu.mulk.mulkcms2.authentication;

import io.quarkus.security.credential.Credential;
import io.quarkus.security.identity.AuthenticationRequestContext;
import io.quarkus.security.identity.IdentityProvider;
import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.security.identity.request.TokenAuthenticationRequest;
import io.quarkus.smallrye.jwt.runtime.auth.JWTAuthMechanism;
import io.smallrye.jwt.auth.principal.DefaultJWTCallerPrincipal;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.Permission;
import java.security.Principal;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.time.Duration;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jose4j.jwa.AlgorithmConstraints;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Interprets a possibly present JWT cookie and uses it to authenticate the user.
 *
 * <p>JWT cookies are used to authenticate further requests based on initial authentication. This
 * way, there is no need to route the user through an OpenID Connect IdP on each request, for
 * example.
 *
 * @see JWTAuthMechanism
 * @see JwtCookieSetterFilter
 */
@ApplicationScoped
public class JwtCookieLoginFilter implements IdentityProvider<TokenAuthenticationRequest> {

  @ConfigProperty(name = "mulkcms.jwt.signing-key")
  String signingKeyAlias;

  @ConfigProperty(name = "mulkcms.jwt.keystore.file")
  String signingKeyFile;

  @ConfigProperty(name = "mulkcms.jwt.keystore.passphrase")
  String signingKeyPassphrase;

  @ConfigProperty(name = "mulkcms.jwt.issuer")
  String issuer;

  @ConfigProperty(name = "mulkcms.jwt.validity")
  Duration validity;

  private static final Logger log = LoggerFactory.getLogger(JwtCookieLoginFilter.class);

  private PublicKey signingKey;

  @PostConstruct
  public void postCostruct()
      throws IOException, KeyStoreException, CertificateException, NoSuchAlgorithmException {
    log.info("Hello!");
    try (var is = new FileInputStream(signingKeyFile)) {
      var keystore = KeyStore.getInstance(KeyStore.getDefaultType());
      keystore.load(is, signingKeyPassphrase.toCharArray());
      signingKey = keystore.getCertificate(signingKeyAlias).getPublicKey();
      Objects.requireNonNull(signingKey);
    }
  }

  @Override
  public CompletionStage<SecurityIdentity> authenticate(
      TokenAuthenticationRequest request, AuthenticationRequestContext context) {

    log.info("Starting JWT verification.");

    return context.runBlocking(
        () -> {
          try {
            log.info("JWT verification started.");

            /*
            AbstractBearerTokenExtractor extractor =
                new BearerTokenExtractor(requestContext, authContextInfo);
            String bearerToken = extractor.getBearerToken();
            */

            // FIXME: But how does this know how the token is extracted?  What passes it here?
            // Look up JWTAuthMechanism.
            var bearerToken = request.getToken().getToken();

            var jwtConsumer =
                new JwtConsumerBuilder()
                    .setJwsAlgorithmConstraints(
                        new AlgorithmConstraints(
                            AlgorithmConstraints.ConstraintType.WHITELIST,
                            AlgorithmIdentifiers.RSA_USING_SHA256,
                            AlgorithmIdentifiers.RSA_USING_SHA384,
                            AlgorithmIdentifiers.RSA_USING_SHA512,
                            AlgorithmIdentifiers.ECDSA_USING_P256_CURVE_AND_SHA256,
                            AlgorithmIdentifiers.ECDSA_USING_P384_CURVE_AND_SHA384,
                            AlgorithmIdentifiers.ECDSA_USING_P521_CURVE_AND_SHA512))
                    .setVerificationKey(signingKey)
                    .setRequireExpirationTime()
                    .setAllowedClockSkewInSeconds(60)
                    .build();

            var claims = jwtConsumer.process(bearerToken).getJwtClaims();
            claims.getSubject();

            var jwtPrincipal = new DefaultJWTCallerPrincipal(claims);
            log.info("JWT verified: {}", jwtPrincipal);

            return new CookieIdentity(jwtPrincipal);
          } catch (InvalidJwtException | MalformedClaimException e) {
            log.info("JWT verification failed", e);
            return null;
          }
        });
  }

  @Override
  public Class<TokenAuthenticationRequest> getRequestType() {
    return TokenAuthenticationRequest.class;
  }

  private static class CookieIdentity implements SecurityIdentity {

    private Principal jwtPrincipal;

    private CookieIdentity(Principal jwtPrincipal) {
      this.jwtPrincipal = jwtPrincipal;
    }

    @Override
    public Principal getPrincipal() {
      return jwtPrincipal;
    }

    @Override
    public boolean isAnonymous() {
      return false;
    }

    @Override
    public Set<String> getRoles() {
      return Set.of();
    }

    @Override
    public <T extends Credential> T getCredential(Class<T> credentialType) {
      return null;
    }

    @Override
    public Set<Credential> getCredentials() {
      return Set.of();
    }

    @Override
    public <T> T getAttribute(String name) {
      return null;
    }

    @Override
    public Map<String, Object> getAttributes() {
      return Map.of();
    }

    @Override
    public CompletionStage<Boolean> checkPermission(Permission permission) {
      return CompletableFuture.completedFuture(false);
    }
  }
}
