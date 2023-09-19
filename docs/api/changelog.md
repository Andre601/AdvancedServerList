---
icon: octicons/log-24
---

# Changelog

This page lists the recent changes made towards the AdvancedServerListAPI.  
They are ordered newest to oldest.

## v3.1.0

### :octicons-plus-circle-24:{ .changelog-added title="Added" } Added { #v3.1.0-added }

- New getters in `ProfileEntry`:
    - `maxPlayersEnabled()` - Returns the value for `playerCount -> maxPlayers -> enabled` as `NullBool`
    - `maxPlayersCount()` - Returns the value of `playerCount -> maxPlayers -> amount` as nullable `Integer`
- New setters in `ProfileEntry.Builder`:
    - `setMaxPlayersEnabled(NullBool)` - Sets whether the max players feature is enabled or not.
    - `setMaxPlayersCount(Integer)` - Sets the number to use for the max players count.
    - `setExtraPlayersCount(Integer)` - Replacement for [deprecated `setExtraPlayerCount(Integer)` method](#v3.1.0-deprecated).

### :octicons-zap-24:{ .changelog-breaking title="Deprecated" } Deprecated { #v3.1.0-deprecated }

- `setExtraPlayerEnabled(Integer)` in `ProfileEntry.Builder` - Typo in method name.

----

## v3.0.0

### :octicons-alert-24:{ .changelog-breaking title="Breaking Changes" } Breaking Changes { #v3-breaking }

- Removed `platform-spigot` API in favour of a more generic sounding `platform-bukkit`
- Renamed all platform-specific packages from `ch.andre601.advancedserverlist.<platform>` to `ch.andre601.advancedserverlist.api.<platform>`

### :octicons-plus-circle-24:{ .changelog-added title="Added" } Added { #v3-added }

- New platform-specific `GenericServer` instances with their own methods to use.
    - `BukkitServer` - Contains a `getWorlds()` method that returns a `Map<String, World>` of all loaded worlds in the server.
    - `BungeeProxy` - Contains a `getServers()` method that returns a `Map<String, ServerInfo>` of all known Servers for BungeeCord.
    - `VelocityProxy` - Contains a `getServers()` method that returns a `Map<String, RegisteredServer>` of all known Servers for Velocity.

### :octicons-trash-24:{ .changelog-removed title="Removed" } Removed { #v3-removed }

- All previously deprecated `getX()` methods from the `ProfileEntry` record

----

## v2.1.0

### :octicons-pencil-24:{ .changelog-changed title="Changed" } Changed { #v2.1.0-changed }

- Turned `ProfileEntry` class into a record
    - Former `getX()` methods are now deprecated in favour of the record's own `x()` methods (i.e. `getMotd()` -> `motd()`)

----

## v2.0.0

### :octicons-alert-24:{ .changelog-breaking title="Breaking Changes" } Breaking Changes { #v2-breaking }

- `platform-paper` has been removed. It used the same code as `platform-spigot` and was therefore considered duplicate code and useless.
- `core` has been removed. The `CachedPlayer` is no longer needed in the platform-specific APIs.

### :octicons-plus-circle-24:{ .changelog-added title="Added" } Added { #v2-added }

- `PreServerListSetEvent` to the different platform-specific APIs.
    - Called before AdvancedServerList alters the server list.
    - Allows to override the `ProfileEntry` used or even cancel the event.
- `ProfileEntry`
  - Used for setting the different values of the server list.
  - Own instances can be made using the nested `Builder` class.
  - Has methods to create a Builder instance from itself, or make a copy from another ProfileEntry-instance.

### :octicons-pencil-24:{ .changelog-changed title="Changed" } Changed { #v2-changed }

- Converted `GenericPlayer`, `GenericServer`, `BungeePlayer`, `SpigotPlayer` and `VelocityPlayer` into interfaces.
    - `GenericPlayer` no longer has `getVersion()`, `hasPlayedBefore()`, `isBanned()` and `isWhitelisted()`. They have been moved to `VelocityPlayer` (Only `getVersion()`) and `SpigotPlayer` respectively.
- Converted `NullBool` from a class to an enum.
    - Contains `TRUE`, `FALSE` and `NOT_SET`.
    - `isNull()` is now `isNotSet()`.

### :octicons-trash-24:{ .changelog-removed title="Removed" } Removed { #v2-removed }

- `UnsupportedAPIAccessException` as it is no longer used in the API itself.

----

## v1.0.0

### :octicons-plus-circle-24:{ .changelog-added title="Added" } Added { #v1-added }

- `AdvancedServerListAPI`
    - Main class used to add your own `PlaceholderProvider` instances.
- `PlaceholderProvider`
    - Abstract class used to add your own placeholders to parse in AdvancedServerList.

<!--

Templates for changelog

### :octicons-alert-24:{ .changelog-breaking title="Breaking Changes" } Breaking Changes { #v-breaking }
### :octicons-plus-circle-24:{ .changelog-added title="Added" } Added { #v-added }
### :octicons-pencil-24:{ .changelog-changed title="Changed" } Changed { #v-changed }
### :octicons-trash-24:{ .changelog-removed title="Removed" } Removed { #v-removed }
### :octicons-zap-24:{ .changelog-deprecated title="Deprecated" } Deprecated { #v-deprecated }

-->
