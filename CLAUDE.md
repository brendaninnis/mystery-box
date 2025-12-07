# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Mystery Box is a Kotlin Multiplatform (KMP) mystery dinner party game application with:
- **Frontend**: Compose Multiplatform (Android, iOS, Web via JS/WASM)
- **Backend**: Ktor server with PostgreSQL
- **Shared**: Cross-platform data models and validation

## Build Commands

```bash
# Full build
./gradlew build

# Backend
./gradlew backend:run              # Run server on port 9090
./gradlew backend:test             # Run backend tests
./gradlew backend:shadowJar        # Create fat JAR for deployment

# Android
./gradlew composeApp:assembleDebug
./gradlew composeApp:bundleRelease

# iOS
./gradlew composeApp:iosX64Binaries

# Web
./gradlew composeApp:jsBrowserDevelopmentRun
./gradlew composeApp:wasmJsBrowserDevelopmentRun

# Tests
./gradlew test                     # All tests
./gradlew :composeApp:testDebugUnitTest  # ComposeApp tests
```

## Architecture

### Module Structure

- **composeApp/**: Multiplatform UI (Android, iOS, Web targets)
- **backend/**: Ktor server with PostgreSQL via Exposed ORM
- **shared/**: Data models and validation shared across all targets
- **kmpiap/**: In-App Purchase library (Android/iOS)
- **iosApp/**: iOS Swift entry point

### Feature Architecture (MVI Pattern)

Each feature in `composeApp/src/commonMain/kotlin/ca/realitywargames/mysterybox/feature/` follows:

```
feature/[name]/
├── data/           # Repository implementations
├── navigation/     # Navigation routes
├── presentation/
│   ├── viewmodel/  # MVI ViewModel extending MviViewModel<State, Action, Effect>
│   ├── action/     # Sealed interface of user actions
│   ├── effect/     # One-time side effects
│   └── state/      # Immutable UI state
└── ui/
    ├── screen/     # Composable screens
    └── component/  # Reusable composables
```

### Key Entry Points

- **App**: `composeApp/src/commonMain/kotlin/ca/realitywargames/mysterybox/App.kt`
- **API Client**: `composeApp/src/commonMain/kotlin/ca/realitywargames/mysterybox/core/data/network/MysteryBoxApi.kt`
- **Backend Server**: `backend/src/main/kotlin/ca/realitywargames/mysterybox/backend/Application.kt`
- **Database Setup**: `backend/src/main/kotlin/ca/realitywargames/mysterybox/backend/database/DatabaseFactory.kt`

### Backend Routes

All API routes under `/api/v1/`:
- `AuthRoutes.kt` - Authentication endpoints
- `MysteryRoutes.kt` - Mystery package CRUD
- `PartyRoutes.kt` - Party/game session management

### Database

- **ORM**: Exposed with PostgreSQL
- **Migrations**: Flyway (auto-runs on startup)
- **Migration files**: `backend/src/main/resources/db/migrations/V[N]__*.sql`
- **Dev database**: `localhost:5432/mysterybox_dev` (user: postgres, pass: password)

## Configuration

### Dependency Management

Uses Gradle version catalog at `gradle/libs.versions.toml`. Reference dependencies with `libs.*` aliases.

### Environment Variables (Production)

```
DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD
JWT_SECRET, JWT_ISSUER, JWT_AUDIENCE
```

### Development Config

Backend config: `backend/src/main/resources/application.conf`

## Conventions

- **Package**: `ca.realitywargames.mysterybox.*`
- **ViewModels**: `[Feature]ViewModel` (e.g., `UserViewModel`)
- **Repositories**: `[Feature]Repository`
- **Backend Routes**: `[Name]Routes.kt`
- **API Responses**: Use `ApiResponse<T>` wrapper from shared models
- **State Updates**: Use `updateState()` in ViewModels for immutable state
