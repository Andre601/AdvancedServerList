---
icon: octicons/log-24
---

# Changelog

This page lists the recent changes made towards the AdvancedServerListAPI.  
They are ordered newest to oldest.

## v3.2.1 { #v3.2.1 }

### :octicons-pencil-24:{ .changelog-changed title="Changed" } Changed { #v3.2.1-changed }

- Removed usage of `${api.version}` in the different pom.xml for version resolving.

## v3.2.0 { #v3.2.0 }

### :octicons-plus-circle-24:{ .changelog-added title="Added" } Added { #v3.2.0-added }

- New setter methods added to [`ProfileEntry.Builder`][profileentry.builder] class, replacing old ones (See deprecations below):
    - [`extraPlayersCount(Integer)`][extraplayerscount]
    - [`extraPlayersEnabled(NullBool)`][extraplayersenabled]
    - [`favicon(String)`][favicon]
    - [`hidePlayersEnabled(NullBool)`][hideplayersenabled]
    - [`maxPlayersCount(Integer)`][maxplayerscount]
    - [`maxPlayersEnabled(NullBool)`][maxplayersenabled]
    - [`motd(List<String>)`][motd]
    - [`playerCountText(String)`][playercounttext]
    - [`players(List<String>)`][players]

[extraplayerscount]: reference/api/ch.andre601.advancedserverlist.api/profiles/builder.md#extraplayerscount(integer)
[extraplayersenabled]: reference/api/ch.andre601.advancedserverlist.api/profiles/builder.md#extraplayersenabled(nullbool)
[favicon]: reference/api/ch.andre601.advancedserverlist.api/profiles/builder.md#favicon(string)
[hideplayersenabled]: reference/api/ch.andre601.advancedserverlist.api/profiles/builder.md#hideplayersenabled(nullbool)
[maxplayerscount]: reference/api/ch.andre601.advancedserverlist.api/profiles/builder.md#maxplayerscount(integer)
[maxplayersenabled]: reference/api/ch.andre601.advancedserverlist.api/profiles/builder.md#maxplayersenabled(nullbool)
[motd]: reference/api/ch.andre601.advancedserverlist.api/profiles/builder.md#motd(list<string>)
[playercounttext]: reference/api/ch.andre601.advancedserverlist.api/profiles/builder.md#playercounttext(string)
[players]: reference/api/ch.andre601.advancedserverlist.api/profiles/builder.md#players(list<string>)

### :octicons-pencil-24:{ .changelog-changed title="Changed" } Changed { #v3.2.0-changed }

- The API has been moved to Codeberg and is now available under a new repository URL to download. The group and artifact ID remained the same, but version no longer has a `v` prefix:
    
    /// tab | ":simple-apachemaven: Maven (pom.xml)"
    ```xml
    <repositories>
      <repository>
        <id>codeberg</id>
        <url>https://codeberg.org/api/packages/Andre601/maven/</url>
      </repository>
    </repositories>
    
    <dependencies>
      <dependency>
        <groupId>ch.andre601.asl-api</groupId>
        <artifactId>api</artifactId>
        <version>{apiVersion}</version>
        <scope>provided</scope>
      </dependency>
    </dependencies>
    ```
    ///
    /// tab | ":simple-gradle: Gradle (build.gradle)"
    ```groovy
    repositories {
        maven = { url ="https://codeberg.org/api/packages/Andre601/maven/" }
    }
    
    dependencies {
        compileOnly "ch.andre601.asl-api:api:{apiVersion}"
    }
    ```
    ///

### :octicons-zap-24:{ .changelog-breaking title="Deprecated" } Deprecated { #v3.2.0-deprecated }

- Setters in [`ProfileEntry.Builder`][profileentry.builder] deprecated in favour of more consistent method naming.
    - `setExtraPlayersCount(Integer)`
    - `setExtraPlayersEnabled(NullBool)`
    - `setFavicon(String)`
    - `setHidePlayersEnabled(NullBool)`
    - `setMaxPlayersCount(Integer)`
    - `setMaxPlayersEnabled(NullBool)`
    - `setMotd(List<String>)`
    - `setPlayerCountText(String)`
    - `setPlayers(List<String>)`

## v3.1.0 { #v3.1.0 }

### :octicons-plus-circle-24:{ .changelog-added title="Added" } Added { #v3.1.0-added }

- New getters in [`ProfileEntry`][profileentry]:
    - [`maxPlayersEnabled()`][maxplayersenabled] - Returns the value for `playerCount -> maxPlayers -> enabled` as `NullBool`
    - [`maxPlayersCount()`][maxplayerscount] - Returns the value of `playerCount -> maxPlayers -> amount` as nullable `Integer`
- New setters in [`ProfileEntry.Builder`][profileentry.builder]:
    - [`setMaxPlayersEnabled(NullBool)`][setmaxplayersenabled] - Sets whether the max players feature is enabled or not.
    - [`setMaxPlayersCount(Integer)`][setmaxplayerscount] - Sets the number to use for the max players count.
    - [`setExtraPlayersCount(Integer)`][setextraplayersecount] - Replacement for [deprecated `setExtraPlayerCount(Integer)` method](#v3.1.0-deprecated).

### :octicons-zap-24:{ .changelog-breaking title="Deprecated" } Deprecated { #v3.1.0-deprecated }

- `setExtraPlayerEnabled(Integer)` in `ProfileEntry.Builder` - Typo in method name.

[profileentry]: reference/api/ch.andre601.advancedserverlist.api/profiles/profileentry.md
[maxplayersenabled]: reference/api/ch.andre601.advancedserverlist.api/profiles/profileentry.md#maxplayersenabled()
[maxplayerscount]: reference/api/ch.andre601.advancedserverlist.api/profiles/profileentry.md#maxplayerscount()

[profileentry.builder]: reference/api/ch.andre601.advancedserverlist.api/profiles/builder.md
[setmaxplayersenabled]: reference/api/ch.andre601.advancedserverlist.api/profiles/builder.md#setmaxplayersenabled(nullbool)
[setmaxplayerscount]: reference/api/ch.andre601.advancedserverlist.api/profiles/builder.md#setmaxplayerscount(integer)
[setextraplayersecount]: reference/api/ch.andre601.advancedserverlist.api/profiles/builder.md#setextraplayerscount(integer)

----

## v3.0.0 { #v3.0.0 }

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

## v2.1.0 { #v2.1.0 }

### :octicons-pencil-24:{ .changelog-changed title="Changed" } Changed { #v2.1.0-changed }

- Turned `ProfileEntry` class into a record
    - Former `getX()` methods are now deprecated in favour of the record's own `x()` methods (i.e. `getMotd()` -> `motd()`)

----

## v2.0.0 { #v2.0.0 }

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

## v1.0.0 { #v1.0.0 }

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
### :octicons-zap-24:{ .changelog-breaking title="Deprecated" } Deprecated { #v-deprecated }

-->
