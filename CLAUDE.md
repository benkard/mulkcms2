# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Development Commands

### Running the Application
```bash
./mvnw quarkus:dev
```
Run with live reloading enabled for development.

### Building and Testing
```bash
./mvnw package                    # Build JAR and Docker image
./mvnw package -Pnative          # Build native image with GraalVM
./mvnw spotless:apply            # Apply Google Java Format
./mvnw spotless:check            # Check code formatting
ant web                          # Bundle web scripts and stylesheets
ant clean                        # Clean all build artifacts
```

### Web Resources
```bash
ant web                          # Build web resources (CSS, JS)
ant web.resources               # Generate web resources only
ant web.check                   # Run Flow and ESLint checks
```

### Database
```bash
./mvnw liquibase:update         # Apply database migrations
./mvnw liquibase:rollback       # Rollback database changes
```

### Deployment
```bash
ant deploy                      # Deploy to Kubernetes
```

## Project Architecture

MulkCMS2 is a Java-based CMS built with Quarkus, featuring a hybrid architecture with two main systems:

### Core Packages
- **benki/** - Modern CMS functionality (posts, bookmarks, wiki, users, chat)
- **cms/** - Legacy CMS system (articles, traditional pages, legacy journal)
- **common/** - Shared utilities (markdown, hibernate, logging, templates)

### Key Architectural Patterns
- **Post Abstraction**: All content types inherit from `Post` base class
- **Dual User Systems**: Both `benki.users.User` and `cms.users.User` exist for legacy compatibility
- **Panache ORM**: Uses Quarkus Panache for simplified Hibernate operations
- **Role-based Security**: Complex visibility controls (PUBLIC, SEMIPRIVATE, PRIVATE, DISCRETIONARY)

### Technology Stack
- **Framework**: Quarkus 3.24.2
- **Database**: PostgreSQL with Hibernate ORM 7.0.4
- **Authentication**: OIDC/Keycloak (prod), embedded users (dev)
- **Templates**: Qute
- **Build**: Maven + Ant for web resources
- **Containerization**: Docker via Jib

## Development Environment

### Database Setup
- Development: PostgreSQL at localhost:5432/mulkcms
- Production: PostgreSQL at postgresql.system:5432/mulkcms
- Uses Liquibase for schema management

### Authentication
- Development: Embedded user system (user: mulk, password: mulk, role: Admin)
- Production: OIDC with Keycloak at login.benkard.de

### Configuration Profiles
- `%dev.*` - Development configuration
- `%prod.*` - Production configuration
- Configuration in `src/main/resources/application.properties`

## Important File Locations

### Configuration
- `src/main/resources/application.properties` - Main configuration
- `src/main/resources/db/changeLog.xml` - Liquibase migrations
- `pom.xml` - Maven dependencies and build configuration
- `build.xml` - Ant build script for web resources

### Templates
- `src/main/resources/templates/` - Qute templates
- `src/main/resources/META-INF/resources/` - Static web resources

### Entity Packages
- `src/main/java/eu/mulk/mulkcms2/benki/` - Entities for microblogging, inherited from Benki
- `src/main/java/eu/mulk/mulkcms2/cms/` - Entities for ordinary blogging, inherited from MulkCMS 1.x

## Code Style and Standards

- **Google Java Format**: Enforced via Spotless plugin
- **Import Order**: java,javax,org,com,de,io,dagger,eu.mulk
- **Java**: Target version 17; prefer to use the newest features
- **Annotations**: Uses JetBrains, JSR305, and basic annotations

## Common Development Tasks

### Adding New Content Types
1. Create entity extending `Post` in benki/ package
2. Add corresponding REST resource class
3. Create Qute templates if needed
4. Update database schema via Liquibase

### Working with Database
- All entities use Panache for simplified ORM
- PostgreSQL-specific features extensively used
- Full-text search available via custom Hibernate functions

### Frontend Development
- Web resources managed by Ant build script
- Uses Snowpack for module bundling
- Parcel for final asset bundling
- ESLint and Flow for code quality

### Security Considerations
- Role-based access control throughout
- Visibility controls on all content
- CSRF protection via Quarkus security
- Multiple authentication backends supported
