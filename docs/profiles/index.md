# Profiles

AdvancedServerList allows the creation of multiple profiles.  
A profile is a single YAML file located in the plugin's `profiles` directory containing multiple settings including [Conditions](#conditions) and [Priority](#priority).

!!! info "Important"
    In order for a profile to be considered "valid" by AdvancedServerList will it need to meet the following conditions:
    
    - Have a [`Priority`](#priority) set to a valid, whole number (i.e. `1`)
    - Have **at least one** of the following options set:
        - [`Profiles`](#profiles) with at least one entry containing one of the below option
        - [`Motd`](#motd) set with at least one line of text
        - [`Favicon`](#favicon) set with a non-empty String
        - [`HidePlayers`](#hideplayers) set to `true`
        - [`Hover`](#hover) set with at least one line of text
        - [`Text`](#text) set with a non-empty String

## Priority

The priority option determines the order in which the profiles are checked by AdvancedServerList, starting with the highest number.

It will go through each file until it finds one with [`conditions`](#conditions) returning true (Default if no conditions are set).  
This means that a profile with no conditions and a priority of `1` will be used before a profile with a condition and priority `0` even if the player is meeting the condition.

## Conditions

A list of conditions can be set that need to be met in order to have this profile shown to the player.  
[Placeholders](./formatting#placeholders) can be used to further customize the conditions (PlaceholderAPI placeholders are **not** supported).

Only when **all** conditions defined return true will the profile be displayed.

You can remove this option, or set it to an empty list (`conditions: []`) to always return true.

### Operants

The following operants are available and can be used in the conditions.

!!! warning "Only one operant can be used per condition!"

| Operant | Description                                                        |
| ------- | ------------------------------------------------------------------ |
| `<`     | Checks if the left value is less than the right one.\*             |
| `<=`    | Checks if the left value is less than or equal to the right one.\* |
| `>`     | Checks if the left value is more than the right one.\*             |
| `>=`    | Checks if the left value is more than or equal to the right one.\* |
| `=`     | Checks if the left value is equal to the right one.                |
| `!=`    | Checks if the left value is not equal to the right one.            |

\* In the case of the provided value not being a number will the plugin instead use the text length to compare with.

## Profiles

!!! info "Available since v1.10.0"

The profiles option allows you to add multiple combinations of all the other options (With the exception of [`Priority`](#priority) and [`Conditions`](#conditions)) to have randomized MOTDs, player counts, etc.

If an option is not present in an entry will AdvancedServerList try to use one defined in the file or use whatever default value would be for that option.

As an example:  
```yaml
priority: 1

profiles:
  - motd:
      - Line 1
      - Line 2
    playerCount:
      text: 'Awesome!'
  - playerCount:
      text: 'Also Awesome'

motd:
  - Line A
  - Line B
```
...the above file would randomly select between a MOTD saying `Line 1\nLine 2` and a player count saying `Awesome!` or a MOTD saying `Line A\nLine B` while the player count says `Also Awesome`

## Motds

!!! warning "Only available in v1.9.0"

This option allows to create multiple MOTDs that AdvancedServerList should randomly select from.

Due to the structure of this option will you need to use `|-` to have multi-line MOTDs.

Example:  
```yaml
motds:
  - |-
    Line 1
    Line 2
  - |-
    Line A
    Line B
```

## Motd

The motd option allows to set up to two lines to be displayed in an MOTD.  
Normal colour and formatting codes such as `<aqua>` or `<bold>` are supported. On 1.16 and newer can you also use HEX colours using either `<#rrggbb>` or `<color:#rrggbb>`.

If you want to display multiple MOTDs will you need to use the [`Profiles` option](#profiles)

## Favicon

The favicon option allows you to customize the favicon and display one or multiple different images.

This option currently supports the following values:

- A URL pointing to a `64x64` PNG image.
- A name with `.png` extension that matches a PNG file in the `favicons` folder.
- `${player uuid}` (or alternatively `${player name}`) to display the head of the player. The UUID/Name resolving is handled by https://mc-heads.net

Please note that AdvancedServerList will rescale the image to be 64x64 pixels, so be sure to provide images at that particular scale.

## PlayerCount

The `playerCount` option contains multiple different options all about the Player count itself.

### HidePlayers

Boolean option to set whether AdvancedServerList should hide the player count or not. When set to `true` will the player count be replaced with `???`.  
Additionally will the [`Text`](#text) and [`Hover`](#hover) option be ignored.

### Hover

This option allows to override the hover text usually displaying the players on the server when hovering over the player count.

You can either remove this option or set to an empty list (`hover: []`) to not override the hover.

### Text

This option allows to override the text usually displaying the online players and total players that can join.  
Note that AdvancedServerList will not add the `<online>/<max>` text to the player count. Instead will you need to use [`${server playersOnline}`](./placeholders#server) and [`${server playersMax}`](./placeholders#server) respectively.

!!! info "Note"
    This feature works by changing the "outdated server" message followed by altering the protocol version of the server.  
    Due to this will your server appear as "outdated" to the client (Have the ping icon crossed out). This is nothing that can be changed unfortunately.

To not override the player count, remove this option or set it to an empty String (`text: ''`).

### ExtraPlayers

Contains options to modify how the `<max>` number in the player count should look like.

!!! warning "Using this option will modify the `${server playersMax}` placeholder (Except in conditions)"

#### Enabled

Boolean option to set whether the extra players feature should be used or not.  
When set to `true` will the max amount of players be modified by taking the current amount of online players, add [`Amount`](#amount) more to it and use it.

#### Amount

Sets the number to add to the current online players to then use as the new max players count.

As an example, setting this to `1` while `10` players are online will display `11`, while `-1` would display `9`.