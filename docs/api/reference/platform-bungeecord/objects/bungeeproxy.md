# <span class="api-type__primitive">interface</span> BungeeProxy

## Method Summary

| Modifier and Type                          | Method                          |
|--------------------------------------------|---------------------------------|
| [`Map<String, ServerInfo>`](#getservers()) | [`getServers()`](#getservers()) |

----

## Method Detail

### <span class="api-type__class">Map&lt;String, ServerInfo&gt;</span> `getServers()` { #getservers() }

Returns a `Map<String, ServerInfo>` where the key is the name of the Server and the value the ServerInfo from the BungeeCord Proxy.

<h4>Returns:</h4>

Possibly-empty Map containing a Server name and ServerInfo instance.