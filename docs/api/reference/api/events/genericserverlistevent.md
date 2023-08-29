# <span class="api-type__primitive">interface</span> GenericServerListEvent

Interface used for the platform-specific PreServerListSetEvent instances.  
This allows the plugin to pull common info such as ProfileEntry or if the event has been cancelled by another plugin.

## Method Summary

| Modifier and Type                 | Method                                              |
|-----------------------------------|-----------------------------------------------------|
| [`ProfileEntry`](#getentry())     | [`getEntry()`](#getentry())                         |
| [`void`](#setentry(profileentry)) | [`setEntry(ProfileEntry)`](#setentry(profileentry)) |
| [`boolean`](#iscancelled())       | [`isCancelled()`](#iscancelled())                   |
| [`void`](#setcancelled(boolean))  | [`setCancelled(boolean)`](#setcancelled(boolean))   |

----

## Method Detail

### <span class="api-type__class">ProfileEntry</span> `getEntry()` { #getentry() }

Gets the [`ProfileEntry`](../profiles/profileentry/index.md) currently set.

<h4>Returns:</h4>

The currently used ProfileEntry.

### <span class="api-type__primitive">void</span> `setEntry(ProfileEntry)` { #setentry(profileentry) }

Sets the new [`ProfileEntry`](../profiles/profileentry/index.md) to use.  
This may not be `null`.

<h4>Parameters:</h4>

- <span class="api__not-null"></span> `entry` - The new [`ProfileEntry`](../profiles/profileentry/index.md) to use.

<h4>Throws:</h4>

- `IllegalArgumentException` - When the provided ProfileEntry is `null`.

### <span class="api-type__primitive">boolean</span> `isCancelled()` { #isCancelled() }

Returns whether this event has been cancelled or not.

<h4>Returns:</h4>

Whether the event has been cancelled or not.

### <span class="api-type__primitive">void</span> `setCancelled(boolean)` { #setcancelled(boolean) }

Sets the event's cancel state.

<h4>Parameters</h4>

- `cancelled` - Boolean to set the event's cancelled state.