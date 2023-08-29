# <span class="api-type__primitive">interface</span> BukkitPlayer

[`GenericPlayer` instance](../../api/objects/genericplayer.md) for the SpigotMC/Paper server implementation of AdvancedServerList.  
This interface includes a [`OfflinePlayer` instance](#getplayer()) obtained from the server the plugin runs on alongside some getters to get if the player [has played on the server before](#hasplayedbefore()), [is banned](#isbanned()) or [is whitelisted](#iswhitelisted()). These options actually require a proper OfflinePlayer instance to be present or will otherwise default to `false`.

This class is useful for cases where you want to use the OfflinePlayer. Simply cast the GenericPlayer instance to a SpigotPlayer (Granted that it actually is an instance of it to begin with).

## Method Summary

| Modifier and Type               | Method                                    |
|---------------------------------|-------------------------------------------|
| [`OfflinePlayer`](#getplayer()) | [`getPlayer()`](#getplayer())             |
| [`boolean`](#hasplayedbefore()) | [`hasPlayedBefore()`](#hasplayedbefore()) |
| [`boolean`](#isbanned())        | [`isBanned()`](#isbanned())               |
| [`boolean`](#iswhitelisted())   | [`isWhitelisted()`](#iswhitelisted())     |

| Methods inherited from `ch.andre601.advancedserverlist.api.objects.`[`GenericPlayer`](../../api/objects/genericplayer.md) |
|---------------------------------------------------------------------------------------------------------------------------|
| [`getName()`](../../api/objects/genericplayer.md#getname()), [`getProtocol()`](../../api/objects/genericplayer.md#getprotocol()), [`getUUID()`](../../api/objects/genericplayer.md#getuuid()) |

----

## Method Detail

### <span class="api-type__class">OfflinePlayer</span> `getPlayer()` { #getplayer() }

Gives the OfflinePlayer embedded in this BukkitPlayer instance.

<h4>Returns:</h4>

OfflinePlayer instance.

### <span class="api-type__primitive">boolean</span> `hasPlayedBefore()` { #hasplayedbefore() }

Returns whether this player has played on the Server before.

The returned boolean may be inaccurate if AdvancedServerList was unable to obtain a valid OfflinePlayer instance.

<h4>Returns:</h4>

Boolean indicating whether this player has played on thise Server before.

### <span class="api-type__primitive">boolean</span> `isBanned()` { #isbanned() }

Returns whether this player is banned on the server.

The returned boolean may be inaccurate if AdvancedServerList was unable to obtain a valid OfflinePlayer instance.

<h4>Returns:</h4>

Boolean indicating whether this player was banned from the server.

### <span class="api-type__primitive">boolean</span> `isWhitelisted()` { #iswhitelisted() }

Returns whether this player is whitelisted on the server.

The returned boolean may be inaccurate if AdvancedServerList was unable to obtain a valid OfflinePlayer instance.

<h4>Returns:</h4>

Boolean indicating whether this player is whitelisted on the server.