# Moose Client
A Kotlin Multiplatform Desktop client for KailleraProtocol, currently in prototyping stage.

## Architecture
- **Framework**: Kotlin Multiplatform / Compose Desktop (`desktopMain`).
- **Networking**: `io.ktor:ktor-network` for UDP handshake logic mapping to the V086 `kailleraprotocol`.
- **UI Logic**: Dynamic bindings using `Jetpack Compose` reacting directly to protocol `MutableStateFlow` bindings.

## Prerequisites
- Requires `kailleraprotocol` published locally via `publishToMavenLocal`.

## Running
```bash
./gradlew run
```
