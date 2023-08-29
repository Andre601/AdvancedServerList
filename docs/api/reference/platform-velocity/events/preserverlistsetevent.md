# <span class="api api__none api-type__primitive">class</span> PreServerListSetEvent

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
| [`ProfileEntry`](#getentry())             | [`getEntry()`](#getentry())                         |
| [`void`](#setentry(profileentry))         | [`setEntry(ProfileEntry)`](#setentry(profileentry)) |
| [`boolean`](#iscancelled())               | [`isCancelled()`](#iscancelled())                   |
| [`void`](#setcancelled(boolean))          | [`setCancelled(boolean)`](#setcancelled(boolean))   |

----

## Constructor Detail

### <span class="api api__public"></span> `PreServerListSetEvent(ProfileEntry)` { #preserverlistsetevent(profileentry) }

<h4>Parameters:</h4>

- `entry`

----

## Method Detail

### <span class="api-type__class">ProfileEntry</span> `getEntry()` { #getentry() }

Gets the [`ProfileEntry`](../../api/profiles/profileentry/index.md) currently set.

<h4>Returns:</h4>

The currently set [`ProfileEntry`](../../api/profiles/profileentry/index.md).

### <span class="api-type__primitive">void</span> `setEntry(ProfileEntry)` { #setentry(profileentry) }

Sets the new [`ProfileEntry`](../../api/profiles/profileentry/index.md) to use.  
This may not be `null`.

Note that a new copy of the entry will be made using [`ProfileEntry.copy()`](../../api/profiles/profileentry/index.md#copy()).

<h4>Parameters:</h4>

- `entry` - The new [`ProfileEntry`](../../api/profiles/profileentry/index.md) to use.

<h4>Throws:</h4>

- `IllegalArgumentException` - Thrown from the `CheckUtil` when the provided ProfileEntry was null.

### <span class="api-type__primitive">boolean</span> `isCancelled()` { #iscancelled() }

Returns whether this event has been cancelled or not.

<h4>Returns:</h4>

Whether this event has been cancelled or not.

### <span class="api-type__primitive">void</span> `setCancelled(boolean)` { #setcancelled(boolean) }

Sets the event's cancel state.

<h4>Parameters:</h4>

- `cancelled` - Boolean to set the event's cancelled state.