---
icon: octicons/note-24
---

# Profiles { #profiles-settings }

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
[Placeholders](formatting.md#placeholders) can be used to further customize the conditions (PlaceholderAPI placeholders are **not** supported).

Only when **all** conditions defined return true will the profile be displayed.

You can remove this option, or set it to an empty list (`conditions: []`) to always return true.

### Operands

The following operants are available and can be used in the conditions.

!!! warning "Only one operant can be used per condition!"

| Operand | Description                                                            |
|---------|------------------------------------------------------------------------|
| `<`     | Checks if the left value is less than the right one.[^1]               |
| `<=`    | Checks if the left value is less than or equal to the right one.[^1]   |
| `>`     | Checks if the left value is more than the right one.[^1]               |
| `>=`    | Checks if the left value is more than or equal to the right one.[^1]   |
| `=`     | Checks if the left value is equal to the right one.                    |
| `!=`    | Checks if the left value is not equal to the right one.                |
| `~=`    | Checks if the left value is equal to the right one, ignoring case.     |
| `!~`    | Checks if the left value is not equal to the right one, ignoring case. |

[^1]:
    Should the provided value not be a number, will AdvancedServerList use the text length to compare.  
    Example: `SomeString > String` would be translated to `10 > 6`.

## Profiles

!!! warning "Priority and Conditions are NOT supported!"

The `profiles` option allows you to set multiple MOTDs, player counts, etc. which the plugin would randomly choose from.  
If an option such as motd is not present in an entry will it check the file for the option and use that if present.

??? example "Example"
    ```yaml title="YAML file"
    priority: 0
    
    profiles:
      - motd:
          - '<rainbow>Line 1</rainbow>'
          - '<rainbow:!>Line 2</rainbow>'
        playerCount:
          text: '<green><bold>Awesome!'
      - playerCount:
          text: '<yellow><bold>Also Awesome!'
    
    motd:
      - '<rainbow:2>Line A</rainbow>'
      - '<rainbow:!2>Line B</rainbow>'
    ```
    <div class="result">
    
    ![profiles-example-1](../assets/images/examples/profiles-example-1.jpg){ loading="lazy" }  
    ![profiles-example-2](../assets/images/examples/profiles-example-2.jpg){ loading="lazy" }

    </div>

## Motd

The motd option allows to set up to two lines to be displayed in an MOTD.  
Normal colour and formatting codes such as `<aqua>` or `<bold>` are supported. On 1.16 and newer can you also use HEX colours using either `<#rrggbb>` or `<color:#rrggbb>`.

If you want to display multiple MOTDs will you need to use the [`Profiles` option](#profiles)

??? example "Example"
    ```yaml title="YAML file"
    priority: 0
    
    motd:
      - '<rainbow:2>Rainbow Gradients</rainbow>'
      - '<rainbow:!2>|||||||||||||||||||||||||||||||||||||</rainbow>'
    ```
    <div class="result">

    ![motd-example](../assets/images/examples/motd-example.jpg){ loading="lazy" }

    </div>


## Favicon

The favicon option allows you to customize the favicon and display one or multiple different images.

This option currently supports the following values:

- A URL pointing to a `64x64` PNG image.
- A name with `.png` extension that matches a PNG file in the `favicons` folder.
- `${player uuid}` (or alternatively `${player name}`) to display the head of the player. The UUID/Name resolving is handled by https://mc-heads.net

Please note that AdvancedServerList will rescale the image to be 64x64 pixels, so be sure to provide images at that particular scale.

??? example "Example"
    ```yaml title="YAML file"
    priority: 0
    
    # We use just colour to hide the MOTD
    motd:
      - '<grey>'
      - '<grey>'
    
    # Player is Andre_601
    favicon: ${player uuid}
    ```
    <div class="result">

    ![hideplayers-example](../assets/images/examples/favicon-example.jpg){ loading="lazy" }

    </div>

## PlayerCount

The `playerCount` option contains multiple different options all about the Player count itself.

### HidePlayers

Boolean option to set whether AdvancedServerList should hide the player count or not. When set to `true` will the player count be replaced with `???`.  
Additionally will the [`Text`](#text) and [`Hover`](#hover) option be ignored.

??? example "Example"
    ```yaml title="YAML file"
    priority: 0
    
    # We use just colour to hide the MOTD
    motd:
      - '<grey>'
      - '<grey>'
    
    playerCount:
      hidePlayers: true
    ```
    <div class="result">

    ![hideplayers-example](../assets/images/examples/hideplayers-example.jpg){ loading="lazy" }

    </div>

### Hover

This option allows to override the hover text usually displaying the players on the server when hovering over the player count.

You can either remove this option or set to an empty list (`hover: []`) to not override the hover.

??? example "Example"
    ```yaml title="YAML file"
    priority: 0
    
    # We use just colour to hide the MOTD
    motd:
      - '<grey>'
      - '<grey>'
    
    playerCount:
      hover:
        - '<grey>Line 1'
        - '<aqua>Line 2'
        - '<gold>Line 3'
    ```
    <div class="result">

    ![hover-example](../assets/images/examples/hover-example.jpg){ loading="lazy" }

    </div>

### Text

This option allows to override the text usually displaying the online players and total players that can join.  
Note that AdvancedServerList will not add the `<online>/<max>` text to the player count. Instead will you need to use [`${server playersOnline}`](placeholders.md#server) and [`${server playersMax}`](placeholders.md#server)

!!! info "Note"
    This feature works by changing the "outdated server" message followed by altering the protocol version of the server.  
    Due to this will your server appear as "outdated" to the client (Have the ping icon crossed out). This is nothing that can be changed unfortunately.

To not override the player count, remove this option or set it to an empty String (`text: ''`).

??? example "Example"
    ```yaml title="YAML file"
    priority: 0
    
    # We use just colour to hide the MOTD
    motd:
      - '<grey>'
      - '<grey>'
    
    playerCount:
      text: '<yellow><bold>Cool text!'
    ```
    <div class="result">

    ![hover-example](../assets/images/examples/text-example.jpg){ loading="lazy" }

    </div>

### ExtraPlayers

Contains options to modify how the `<max>` number in the player count should look like.

!!! warning "Using this option will modify the `${server playersMax}` placeholder (Except in conditions)"

#### Enabled { #extraplayers-enabled }

Boolean option to set whether the extra players feature should be used or not.  
When set to `true` will the max amount of players be modified by taking the current amount of online players, add [`Amount`](#amount) more to it and use it.

#### Amount { #extraplayers-amount }

Sets the number to add to the current online players to then use as the new max players count.

As an example, setting this to `1` while `10` players are online will display `11`, while `-1` would display `9`.

??? example "Example"
    ```yaml title="YAML file"
    priority: 0
    
    # We use just colour to hide the MOTD
    motd:
      - '<grey>'
      - '<grey>'
    
    playerCount:
      extraPlayers:
        enabled: true
        amount: 1
    ```
    <div class="result">

    ![extraplayers-example](../assets/images/examples/extraplayers-example.jpg){ loading="lazy" }

    </div>

### MaxPlayers

Contains options to set the max number of players allowed to join the server. This has only a visual effect and won't actually influence how many players can join your server.

Note that this feature is ignored should [`extraPlayers`](#extraplayers) be enabled.

In case [`hidePlayers`](#hideplayers) is enabled will the max player count not be displayed, but still changed by this option.  
Should [`text`](#text) be set to a non-empty String will the max player count only be displayed when `${server playersMax}` is used.

!!! warning "Using this option will modify the `${server playersMax}` placeholder (Except in conditions)"

#### Enabled { #maxplayers-enabled }

Boolean option to set whether the max players feature should be used or not.  
When set to `true` will the value usually displaying how many players can join be changed to what you've set in the [`amount`](#maxplayers-amount) option.

#### Amount { #maxplayers-amount }

Sets the number to display as the max number of players allowed to join your server.  
Note that this will also affect the `${server playersMax}` placeholder, except when it is used inside conditions.

??? example "Example"
    ```yaml title="YAML file"
    priority: 0
    
    # We use just colour to hide the MOTD
    motd:
      - '<grey>'
      - '<grey>'
    
    playerCount:
      maxPlayers:
        enabled: true
        amount: -1
    ```
    <div class="result">

    ![maxplayes-example](../assets/images/examples/maxplayers-example.jpg){ loading="lazy" }

    </div>
