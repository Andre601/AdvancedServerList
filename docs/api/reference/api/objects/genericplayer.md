# <span class="api-type__primitive">interface</span> GenericPlayer

A basic interface used to store generic player data to use in placeholder resolving.  
The different variants of AdvancedServerList implement this into their own class and may provide additional data not available through this interface.

The following values will always be present, no matter the platform this interface is used on:

- [Name of the Player](#getname())
- [Protocol version of the Player](#getprotocol())
- [UUID of the Player](#getuuid())

## Method Summary

| Modifier and Type       | Method                            |
|-------------------------|-----------------------------------|
| [`String`](#getname())  | [`getName()`](#getname())         |
| [`int`](#getprotocol()) | [`getProtocol()`](#getprotocol()) |
| [`UUID`](#getuuid())    | [`getUUID()`](#getuuid())         |

----

## Method Detail

### <span class="api-type__class">String</span> `getName()` { #getname() }

Returns the name of the player.  
Note that on Spigot, Paper and other forks can the name differ from the one cached by AdvancedServerList, if the plugin was able to retrieve an OfflinePlayer instance from the server. On BungeeCord and Velocity will the returned String always be the name from the cache.

Name may be whatever has been defined in AdvancedServerList's config.yml, should the player not be cached yet by AdvancedServerList.

<h4>Returns:</h4>

String representing the player's name.

### <span class="api-type__primitive">int</span> `getProtocol()` { #getprotocol() }

Returns the protocol ID the player is using. The protocol ID is an integer used by Minecraft to determine what version a server or client is running.

<h4>Returns:</h4>

Integer representing the protocol version of this player.

### <span class="api-type__class">UUID</span> `getUUID()` { #getuuid() }

Returns the unique ID associated with this player.

UUID may be whatever has been defined in AdvancedServerList's config.yml, should the player not be cached yet by AdvancedServerList.

<h4>Returns:</h4>

UUID of the player.