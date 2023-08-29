# <span class="api__none api-type__primitive">interface</span> GenericServer

Simple class used to wrap around some generic server data such as online player count, amount of total players that can join and the host (IP/Domain) that got pinged by the player.

## Method Summary

| Modifier and Type            | Method                                      |
|------------------------------|---------------------------------------------|
| [`int`](#getplayersonline()) | [`getPlayersOnline()`](#getplayersonline()) |
| [`int`](#getplayersmax())    | [`getPlayersMax()`](#getplayersmax())       |
| [`String`](#gethost())       | [`getHost()`](#gethost())                   |

----

## Method Detail

### <span class="api-type__primitive">int</span> `getPlayersOnline()` { #getplayersonline() }

Returns the number of players currently online on the server.

<h4>Returns:</h4>

Number of players online on the server.

### <span class="api-type__primitive">int</span> `getPlayersMax()` { #getplayersmax() }

Returns the number of total players that can join the server.

<h4>Returns:</h4>

Number of total players that can join the server.

### <span class="api__nullable"></span> <span class="api-type__class">String</span> `getHost()` { #gethost() }

Returns the IP/Domain that got pinged by the player.

<h4>Returns:</h4>

Possibly-null String containing the IP/Domain that got pinged by the player.