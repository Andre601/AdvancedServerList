# <span class="api-type__primitive">interface</span> BukkitServer

Spigot/Paper specific instance of a [`GenericServer`](../../api/objects/genericserver.md).  
This interface includes a [Map of Worlds](#getworlds()) the server currently has.

This interface is useful for cases where you want to get the worlds of the Server itself, by simply casting your GenericServer instance to a BukkitServer one. Make sure to do proper instanceof checks first before attempting to cast.

## Method Summary

| Modifier and Type                    | Method                        |
|--------------------------------------|-------------------------------|
| [`Map<String, World>`](#getworlds()) | [`getWorlds()`](#getworlds()) |

----

## Method Detail

### <span class="api-type__class">Map&lt;String, World&gt;</span> `getWorlds()` { #getworlds() }

Returns a `Map<String, World>` where the key is the name of the World and the value the world of the server.

<h4>Returns:</h4>

Possibly-empty Map containing a world name and World instance.