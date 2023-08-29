# <span class="api-type__primitive">class</span> PreServerListSetEvent

Called **before** AdvancedServerList modifies the server list.  
The provided [`ProfileEntry`](#getentry()) will be the one used for the server list.

## Constructor Summary

| Constructor                                                                   | Description |
|-------------------------------------------------------------------------------|-------------|
| [`PreServerListSetEvent(ProfileEntry)`](#preserverlistsetevent(profileentry)) | *None*      |

----

## Method Summary

| Modifier and Type                         | Method                                              |
|-------------------------------------------|-----------------------------------------------------|
| [`static HandlerList`](#gethandlerlist()) | [`getHandlerList()`](#gethandlerlist())             |
| [`HandlerList`](#gethandlers())           | [`getHandlers()`](#gethandlers())                   |

| Methods inherited from `ch.andre601.advancedserverlist.api.events.`[`GenericServerListEvent`](../../api/events/genericserverlistevent.md) |
|-------------------------------------------------------------------------------------------------------------------------------------------|
| [`getEntry()`](../../api/events/genericserverlistevent.md#getentry()), [`setEntry(ProfileEntry)`](../../api/events/genericserverlistevent.md#setentry(profileentry)), [`isCancelled()`](../../api/events/genericserverlistevent.md#iscancelled()), [`setCancelled(boolean)`](../../api/events/genericserverlistevent.md#setcancelled(boolean)) |

----

## Constructor Detail

### `PreServerListSetEvent(ProfileEntry)` { #preserverlistsetevent(profileentry) }

<h4>Parameters:</h4>

- `entry`

----

## Method Detail

### <span class="api-label api__static"></span> <span class="api-type__class">HandlerList</span> `getHandlerList()` { #gethandlerlist() }

<h4>Returns:</h4>

`HandlerList` instance.

### <span class="api-type__class">HandlerList</span> `getHandlers()` { #gethandlers() }

<h4>Returns:</h4>

`HandlersList` instance.