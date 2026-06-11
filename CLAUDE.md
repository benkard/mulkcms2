# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Development Commands

### Running the Application
```bash
./mvnw quarkus:dev
```
Run with live reloading enabled for development. Requires PostgreSQL at localhost:5432/mulkcms (user `mulk`, empty password); Liquibase migrations run automatically at startup.

### Building
```bash
./mvnw package                   # Build JAR (and Docker image config via Jib)
./mvnw package -Pnative          # Build native image with GraalVM
ant web                          # Bundle web scripts and stylesheets
ant clean                        # Clean all build artifacts (incl. node_modules, dist)
ant deploy                       # Build, push Docker image, deploy to Kubernetes
```

### Code Formatting
```bash
./mvnw spotless:apply            # Apply Google Java Format
./mvnw spotless:check            # Check code formatting
```

### Web Resources
The frontend lives in `src/main/resources/META-INF/resources/` and is built with yarn + esbuild (`build.js`):
```bash
ant web.resources                # Generate web resources only (yarn install + esbuild)
ant web.check                    # Run Flow and ESLint checks
```
Or directly in `src/main/resources/META-INF/resources/`:
```bash
yarn run build                   # esbuild bundle (node build.js)
yarn run dev                     # esbuild in watch mode
```
Note: the Ant `esbuild` target is skipped if `web_modules/` and `dist/` already exist; run `ant clean` or `yarn run build` directly to force a rebuild.

### Database
```bash
./mvnw liquibase:update          # Apply database migrations
./mvnw liquibase:rollback        # Rollback database changes
```
Migrations live in `src/main/resources/db/changeLog-*.xml`, included from `changeLog.xml`.

### Testing
There is no test suite (no `src/test/` directory and no test dependencies). Verify changes by running the app in dev mode.

## Project Architecture

MulkCMS2 is a Java-based CMS built with Quarkus, featuring a hybrid architecture with two main systems:

### Core Packages (`src/main/java/eu/mulk/mulkcms2/`)
- **benki/** - Modern CMS functionality: posts, bookmarks, lazychat (microblog chat), wiki, newsletter, users, access control
- **cms/** - Legacy CMS system inherited from MulkCMS 1.x: journal, legacy journal, pages, comments
- **common/** - Shared utilities (markdown, hibernate, logging, templates)

### Key Architectural Patterns
- **Post Abstraction**: Content types (`Bookmark`, `LazychatMessage`) inherit from the `Post` base class (`benki/posts/Post.java`), which centralizes visibility/access-control queries via the JPA Criteria API. Post bodies live in separate `*Text` entities keyed by (post, language).
- **Dual User Systems**: Both `benki.users.User` and `cms.users.User` exist for legacy compatibility
- **Panache ORM**: Entities extend `PanacheEntityBase` for simplified Hibernate operations
- **Visibility Model**: Posts are PUBLIC, SEMIPRIVATE, DISCRETIONARY, or PRIVATE, computed from access-control `Role` targets rather than stored directly

### Technology Stack
- **Framework**: Quarkus 3.24.2
- **Database**: PostgreSQL with Hibernate ORM 7.0.4, Liquibase for schema management
- **Authentication**: OIDC/Keycloak in prod, embedded users in dev; additionally JWT-in-cookie (`smallrye-jwt`) signed with a local keystore
- **Templates**: Qute (`src/main/resources/templates/`, organized per resource class, e.g. `templates/PostResource/`)
- **Frontend**: esbuild bundling, Flow type checking, ESLint; CSS from purecss/sanitize.css; ContentTools editor
- **Build**: Maven (Java) + Ant (web resources, deployment)
- **Containerization**: Docker via Jib, deployed to Kubernetes with kubectl (`ant deploy`)

## Development Environment

### Authentication
- Development: OIDC disabled; embedded user system (user: mulk, password: mulk, role: Admin)
- Production: OIDC with Keycloak at login.benkard.de

### Configuration Profiles
- `%dev.*` - Development configuration
- `%prod.*` - Production configuration
- Configuration in `src/main/resources/application.properties`

## Important File Locations

- `src/main/resources/application.properties` - Main configuration
- `src/main/resources/db/changeLog.xml` - Liquibase migration entry point
- `pom.xml` - Maven dependencies and build configuration
- `build.xml` - Ant build script for web resources and deployment
- `src/main/resources/templates/` - Qute templates
- `src/main/resources/META-INF/resources/` - Frontend sources and static web resources (yarn project)

## Code Style and Standards

- **Google Java Format**: Enforced via Spotless plugin; run `./mvnw spotless:apply` before committing
- **Import Order**: java,javax,org,com,de,io,dagger,eu.mulk
- **Java**: Target version 17; prefer to use the newest features
- **JavaScript**: Flow-typed, checked with ESLint (see `ant web.check`)

## Common Development Tasks

### Adding New Content Types
1. Create entity extending `Post` in benki/ package
2. Add corresponding REST resource class
3. Create Qute templates if needed
4. Update database schema via a new Liquibase changelog included from `changeLog.xml`

### Working with the Database
- All entities use Panache for simplified ORM
- PostgreSQL-specific features extensively used
- Full-text search available via custom Hibernate functions (see `common/hibernate/`)
