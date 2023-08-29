# <span class="api-type__primitive">class</span> PreServerListSetEvent

Called **before** AdvancedServerList modifies the server list.  
The provided [`ProfileEntry`](#getentry()) will be the one used for the server list.

## Constructor Summary

| Constructor                                                                   | Description |
|-------------------------------------------------------------------------------|-------------|
| [`PreServerListSetEvent(ProfileEntry)`](#preserverlistsetevent(profileentry)) | *None*      |

----

## Method Summary

| Methods inherited from `ch.andre601.advancedserverlist.api.events.`[`GenericServerListEvent`](../../api/events/genericserverlistevent.md) |
|-------------------------------------------------------------------------------------------------------------------------------------------|
| [`getEntry()`](../../api/events/genericserverlistevent.md#getentry()), [`setEntry(ProfileEntry)`](../../api/events/genericserverlistevent.md#setentry(profileentry)), [`isCancelled()`](../../api/events/genericserverlistevent.md#iscancelled()), [`setCancelled(boolean)`](../../api/events/genericserverlistevent.md#setcancelled(boolean)) |

----

## Constructor Detail

### `PreServerListSetEvent(ProfileEntry)` { #preserverlistsetevent(profileentry) }

<h4>Parameters:</h4>

- `entry`