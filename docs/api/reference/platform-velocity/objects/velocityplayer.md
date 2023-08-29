# <span class="api-type__primitive">interface</span> VelocityPlayer

[`GenericPlayer` instance](../../api/objects/genericplayer.md) for the Velocity proxy implementation of AdvancedServerList.  
Provides a [`getVersion()` method](#getversion()) to get the MC version used as a readable String (i.e. 1.19.3) rather than just the protocol version.

To get an instance of this class from a GenericPlayer instance, simply cast it to a VelocityPlayer (Granted that the GenericPlayer instance actually is a VelocityPlayer instance).

## Method Summary

| Modifier and Type                         | Method                                              |
|-------------------------------------------|-----------------------------------------------------|
| [`String`](#getversion())                 | [`getVersion()`](#getversion())                     |

| Methods inherited from `ch.andre601.advancedserverlist.api.objects.`[`GenericPlayer`](../../api/objects/genericplayer.md) |
|---------------------------------------------------------------------------------------------------------------------------|
| [`getName()`](../../api/objects/genericplayer.md#getname()), [`getProtocol()`](../../api/objects/genericplayer.md#getprotocol()), [`getUUID()`](../../api/objects/genericplayer.md#getuuid()) |

----

## Method Detail

### <span class="api-type__class">String</span> `getVersion()` { #getversion() }

Returns the [protocol version](../../api/objects/genericplayer.md#getprotocol()) in a readable MC version format (i.e. 1.19.3).

<h4>Returns:</h4>

The readable MC version the player uses.