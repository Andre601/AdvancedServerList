# Changelog

This page lists the recent changes made towards the AdvancedServerListAPI.  
They are ordered newest to oldest.

## v2.1.0

### :octicons-pencil-24:{ .changelog-changed title="Changed" } Changed { #v-changed }

- Turned `ProfileEntry` class into a record
    - Former `getX()` methods are now deprecated in favour of the recor's own `x()` methods (i.e. `getMotd()` -> `motd()`)

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

-->