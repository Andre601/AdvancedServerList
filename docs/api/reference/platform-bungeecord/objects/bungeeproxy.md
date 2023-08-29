# <span class="api-type__primitive">interface</span> BungeeProxy

## Method Summary

| Modifier and Type                          | Method                          |
|--------------------------------------------|---------------------------------|
| [`Map<String, ServerInfo>`](#getservers()) | [`getServers()`](#getservers()) |

| Methods inherited from `ch.andre601.advancedserverlist.api.objects.`[`GenericServer`](../../api/objects/genericserver.md) |
|---------------------------------------------------------------------------------------------------------------------------|
| [`getPlayersOnline()`](../../api/objects/genericserver.md#getplayersonline()), [`getPlayersMax()`](../../api/objects/genericserver.md#getplayersmax()), [`getHost()`](../../api/objects/genericserver.md#gethost()) |

----

## Method Detail

### <span class="api-type__class">Map&lt;String, ServerInfo&gt;</span> `getServers()` { #getservers() }

Returns a `Map<String, ServerInfo>` where the key is the name of the Server and the value the ServerInfo from the BungeeCord Proxy.

<h4>Returns:</h4>

Possibly-empty Map containing a Server name and ServerInfo instance.