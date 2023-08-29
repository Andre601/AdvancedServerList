# <span class="api-type__primitive">interface</span> VelocityProxy

## Method Summary

| Modifier and Type                                | Method                          |
|--------------------------------------------------|---------------------------------|
| [`Map<String, RegisteredServer>`](#getservers()) | [`getServers()`](#getservers()) |

----

## Method Detail

### <span class="api-type__class">Map&lt;String, RegisteredServer&gt;</span> `getServers()` { #getservers() }

Returns a `Map<String, RegisteredServer>` where the key is the name of the Server and the value the RegisteredServer from the BungeeCord Proxy.

<h4>Returns:</h4>

Possibly-empty Map containing a Server name and RegisteredServer instance.