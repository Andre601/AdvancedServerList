# <span class="api-type__primitive">class</span> Builder

Builder class to create a new [`ProfileEntry` instance](index.md).

## Constructor Summary

| Constructor               | Description |
|---------------------------|-------------|
| [`Builder()`](#builder()) | *None*      |

----

## Method Summary

| Modifier and Type                              | Method                                                                  |
|------------------------------------------------|-------------------------------------------------------------------------|
| [`Builder`](#setmotd(list-string))             | [`setMotd(List<String>)`](#setmotd(list-string))                        |
| [`Builder`](#setplayers(list-string))          | [`setPlayers(List<String>)`](#setplayers(list-string))                  |
| [`Builder`](#setplayercounttext(string))       | [`setPlayerCountText(String)`](#setplayercounttext(string))             |
| [`Builder`](#setfavicon(string))               | [`setFavicon(String)`](#setfavicon(string))                             |
| [`Builder`](#sethideplayersenabled(nullbool))  | [`setHidePlayersEnabled(NullBool)`](#sethideplayersenabled(nullbool))   |
| [`Builder`](#setextraplayersenabled(nullbool)) | [`setExtraPlayersEnabled(NullBool)`](#setextraplayersenabled(nullbool)) |
| [`Builder`](#setextraplayerscount(integer))    | [`setExtraPlayersCount(Integer)`](#setextraplayerscount(integer))       |
| [`ProfileEntry`](#build())                     | [`build()`](#build())                                                   |

----

## Constructor Detail

### `Builder()` { #builder() }

----

## Method Detail

### <span class="api-type__class">Builder</span> `setMotd(List<String>)` { #setmotd(List-string) }

Sets a new MOTD to use.

Set to an empty list to not change the MOTD.  
Only the first two entries of the list will be considered and any additional ones discarded.

An `IllegalArgumentException` may be thrown by the `CheckUtil` should the provided motd list be null.

<h4>Parameters:</h4>

- <span class="api__not-null"></span> `motd` - The MOTD to use.

<h4>Returns:</h4>

This Builder after the motd has been set. Useful for chaining.

<h4>Throws</h4>

- `IllegalArgumentException` - Thrown by the `CheckUtil` in case `null` has been provided as list.

### <span class="api-type__class">Builder</span> `setPlayers(List<String>)` { #setplayers(list-string) }

Sets the players (lines) to use for the hover.

Set to an empty list to not change the hover text.

An `IllegalArgumentException` may be thrown by the `CheckUtil` should the provided motd list be null.

<h4>Parameters:</h4>

- <span class="api__not-null"></span> `players` - The lines to set for the hover.

<h4>Returns:</h4>

This Builder after the players has been set. Useful for chaining.

<h4>Throws</h4>

- `IllegalArgumentException` - Thrown by the `CheckUtil` in case `null` has been provided as list.

### <span class="api-type__class">Builder</span> `setPlayerCountText(String)` { #setplayercounttext(String) }

Sets the text to override the player count with.

Set to an empty String or `null` to not alter the Player count text.

<h4>Parameters:</h4>

- <span class="api__nullable"></span> `playerCountText` - The text to show in the player count.

<h4>Returns:</h4>

This Builder after the player count text has been set. Useful for chaining.

### <span class="api-type__class">Builder</span> `setFavicon(String)` { #setfavicon(string) }

Sets the value to use for the favicon.  
The following values are supported:

- URL to a valid PNG file
- File name (with `.png` extension) matching a file saved in the favicons folder of AdvancedServerList
- `${player uuid}` to display the avatar of the player.

Set to an empty String or `null` to not alter the Favicon.

<h4>Parameters:</h4>

- <span class="api__nullable"></span> `favicon` - The favicon to use.

<h4>Returns:</h4>

This Builder after the favicon has been set. Useful for chaining.

### <span class="api-type__class">Builder</span> `setHidePlayersEnabled(NullBool)` { #sethideplayersenabled(nullbool) }

Sets whether the player count should be hidden or not.

Set to [`NullBool.NOT_SET`](../../objects/nullbool.md#not_set) to not set this.

An `IllegalArgumentException` may be thrown by the `CheckUtil` should hidePlayersEnabled be null.

<h4>Parameters:</h4>

- <span class="api__not-null"></span> `hidePlayersEnabled` - Whether the player count should be hidden or not.

<h4>Returns:</h4>

This Builder after NullBool has been set. Useful for chaining.

### <span class="api-type__class">Builder</span> `setExtraPlayersEnabled(NullBool)` { #setextraplayersenabled(nullbool) }

Sets whether the extra players feature should be enabled.

Set to [`NullBool.NOT_SET`](../../objects/nullbool.md#not_set) to not set this.

An `IllegalArgumentException` may be thrown by the `CheckUtil` should extraPlayersEnabled be null.

<h4>Parameters:</h4>

- <span class="api__not-null"></span> `extraPlayersEnabled` - Whether the extra players feature should be enabled or not.

<h4>Returns:</h4>

This Builder after NullBool has been set. Useful for chaining.

### <span class="api-type__class">Builder</span> `setExtraPlayerCount(Integer)` { #setextraplayercount(integer) }

Sets the number of players to add to the online players to use as the new max players value.  
This option has no effect when [`extraPlayersEnabled()`](index.md#extraplayersenabled()) is set to `false`.

Set this to `null` to not alter the max player count. Alternatively [disable extra Players](#setextraplayersenabled(nullbool)).

<h4>Parameters:</h4>

- <span class="api__nullable"></span> `extraPlayersCount` - The number of extra players to add.

<h4>Returns:</h4>

This Builder after the extra player count has been set. Useful for chaining.

### <span class="api-type__class">ProfileEntry</span> `build()` { #build() }

Creates a new [`ProfileEntry` instance](index.md) with the values set in this Builder.

<h4>Returns:</h4>

New [`ProfileEntry` instance](index.md).