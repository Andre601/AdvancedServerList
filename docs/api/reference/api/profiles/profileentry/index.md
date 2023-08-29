# <span class="api-type__primitive">record</span> ProfileEntry

This record represents the content found in a server list profile YAML file.  
The content may come from either the "profiles" list, the options in the file itself (global options) or a mix of both.

This class is immutable. Use [`builder()`](#builder()) to get a [`Builder` instance](builder.md) with the values of this class added.

## Constructor Summary

| Constructor | Description |
|-------------------------------------------------------------------------------------------------------------------------------------------------------------|----------------------------------------------------------------|
| [`ProfileEntry(List<String>, List<String>, String, String, NullBool, NullBool, Integer)`](#profileentry(list-list-string-string-nullbool-nullbool-integer)) | Creates a new instance of a ProfileEntry with the given values |

----

## Nested Class Summary

| Modifier and Type            | Class                                |
|------------------------------|--------------------------------------|
| [`static class`](builder.md) | [`ProfileEntry.Builder`](builder.md) |

----

## Method Summary

| Modifier and Type                    | Method                                            |
|--------------------------------------|---------------------------------------------------|
| [`static ProfileEntry`](#empty())    | [`empty()`](#empty())                             |
| [`ProfileEntry`](#copy)              | [`copy()`](#copy())                               |
| [`Builder`](#builder())              | [`builder()`](#builder())                         |
| [`List<String>`](#motd())            | [`motd()`](#modt())                               |
| [`List<String>`](#players())         | [`players()`](#players())                         |
| [`String`](#playercounttext())       | [`playerCountText()`](#playercounttext())         |
| [`String`](#favicon())               | [`favicon()`](#favicon())                         |
| [`NullBool`](#hideplayersenabled())  | [`hidePlayersEnabled()`](#hideplayersenabled())   |
| [`NullBool`](#extraplayersenabled()) | [`extraPlayersEnabled()`](#extraplayersenabled()) |
| [`Integer`](#extraplayerscount())    | [`extraPlayersCount()`](#extraplayerscount())     |
| [`boolean`](#isinvalid())            | [`isInvalid()`](#isinvalid())                     |

----

## Constructor Detail

### `ProfileEntry(List<String>, List<String>, String, String, NullBool, NullBool, Integer)` { #profileentry(list-list-string-string-nullbool-nullbool-integer) }

Creates a new instance of a ProfileEntry with the given values.  
It's recommended to use the [`Builder` class](builder.md) for a more convenient configuration of the settings.

<h4>Parameters:</h4>

- `motd` - The MOTD to use.
- `players` - The players (lines) to show in the hover.
- <span class="api__nullable"></span> `playerCountText` - The text to display instead of the player count
- <span class="api__nullable"></span> `favicon` - The favicon to use.
- `hidePlayersEnabled` - Whether player count should be hidden.
- `extraPlayersEnabled` - Whether the extra players option should be enabled.
- <span class="api__nullable"></span> `extraPlayersCount` - The number to add to the online players for the extra players.

<h4>See also:</h4>

- [`ProfileEntry.Builder`](builder.md)

----

## Method Detail

### <span class="api-label api__static"></span> <span class="api-type__class">ProfileEntry</span> `empty()` { #empty() }

Creates an "empty" PlayerEntry with the following values set:

- [`motd`](#motd()): Empty List
- [`players`](#players()): Empty List
- [`playerCountText`](#playercounttext()): Empty String
- [`favicon`](#favicon()): Empty String
- [`hidePlayersEnabled`](#hidePlayersEnabled()): [`NullBool.NOT_SET`](../../objects/nullbool.md#not_set)
- [`extraPlayersEnabled`](#extraplayersenabled()): [`NullBool.NOT_SET`](../../objects/nullbool.md#not_set)
- [`extraPlayersCount`](#extraplayerscount()): `null`

<h4>Returns:</h4>

New ProfileEntry instance with empty/null values defined.

### <span class="api-type__class">ProfileEntry</span> `copy()` { #copy() }

Creates a copy of this ProfileEntry instance.

This is simply a convenience method to not have to call [`builder()`](#builder())`.`[`build()`](builder.md#build()):  
```java
ProfileEntry entry = // get ProfileEntry instance

// Both do the same.
ProfileEntry newEntry1 = entry.builder().build();
ProfileEntry newEntry2 = entry.copy();
```

If you want to modify the ProfileEntry should [`builder()`](#builder()) be preferred.

<h4>Returns:</h4>

A copy of this ProfileEntry instance.

<h4>See also:</h4>

- [`builder()`](#builder())

### <a href="builder" class="api-type__class">Builder</a> `builder()` { #builder() }

Creates a [`Builder` instance](builder.md) with the values from this ProfileEntry set.  
Use this method if you would like to modify the ProfileEntry.

<h4>Returns:</h4>

A new [`Builder` instance](builder.md) with the values of this ProfileEntry set.

### <span class="api-type__class">List&lt;String&gt;</span> `motd()` { #motd() }

Gets the currently set MOTD of this ProfileEntry.

<h4>Returns:</h4>

The current MOTD used by this ProfileEntry.

### <span class="api-type__class">List&lt;String&gt;</span> `players()` { #players() }

Gets the currently set list of players of this ProfileEntry.

<h4>Returns:</h4>

The current list of players used by this ProfileEntry.

### <span class="api-type__class">String</span> `playerCountText()` { #playercounttext() }

Gets the currently set player count text of this ProfileEntry.

<h4>Returns:</h4>

The current player count text used by this ProfileEntry.

### <span class="api-type__class">String</span> `favicon()` { #favicon() }

Gets the currently set favicon of this ProfileEntry.  
Note that the favicon usually is and supports one of the following options:

- URL to a valid PNG file
- File name (with `.png` extension) matching a file saved in the favicons folder of AdvancedServerList
- `${player uuid}` to display the avatar of the player

<h4>Returns:</h4>

The current favicon used by this ProfileEntry.

### <a href="../../objects/nullbool" class="api-type__class">NullBool</a> `hidePlayersEnabled()` { #hideplayersenabled() }

Whether the player count should be hidden or not.  
To get the actual boolean value, use [`getOrDefault(boolean)`](../../objects/nullbool.md#getordefault(boolean)).

<h4>Returns:</h4>

Whether the player count should be hideen or not.

### <a href="../../objects/nullbool" class="api-type__class">NullBool</a> `extraPlayersEnabled()` { #extraplayersenabled() }

Whether the extra players feature should be used or not.  
To get the actual boolean value, use [`getOrDefault(boolean)`](../../objects/nullbool.md#getordefault(boolean)).

<h4>Returns:</h4>

Whether the extra players feature should be used or not.

### <span class="api-type__class">Integer</span> `extraPlayersCount()` { #extraplayerscount() }

Gets the currently set number of extra players of this ProfileEntry. May be `null`.

<h4>Returns:</h4>

The current number of extra players used by this ProfileEntry, or `null`.

### <span class="api-type__primitive">boolean</span> `isInvalid()` { #isinvalid() }

Whether this ProfileEntry is invalid or not.  
The ProfileEntry is considered invalid if all the following is true:

- [`motd`](#motd()) is empty
- [`players`](#players()) is empty
- [`playerCountText`](#playercounttext()) is null/empty **and** [`hidePlayersEnabled`](#hideplayersenabled()) is `false`
- [`favicon`](#favicon()) is null/empty

As long as one of the above is **not** true is this profileEntry considered valid.

<h4>Returns:</h4>

Whether this ProfileEntry is valid or not.