---
icon: octicons/note-24
---

# Profiles { #profiles-file }

AdvancedServerList allows you to create multiple so called Server List Profiles.  
A Server List Profile is a YAML file located in the plugin's `profiles` folder. Each Server List Profile allows you to modify one or multiple aspects of your server's display in a Multiplayer Server list.

Additionally can [Conditions](#condition) and a [Priority](#priority) be set to set when a specific profile should be displayed to a player.

## Required Options

For a Server List Profile to be valid, will it require the following options to be set:

- A valid [`priority`](#priority) option with a single, whole number.
- **At least** one of the following options:
    - [`profiles`](#profiles) with at least one of the below options set.
    - [`motd`](#motd) with at least one line to display.
    - [`favicon`](#favicon) with a non-empty String.
    - [`playerCount -> hidePlayers`](#hideplayers) with value `true`.
    - [`playerCount -> hover`](#hover) with at least one line to display.
    - [`playerCount -> text`](#text) with a non-empty String.

## Priority

The Priority option allows you to define what Server List Profile should be chosen first.  
The plugin will go highest value to lowest value and will select the first Profile that has a [`condition`](#condition) returning `true`.

Should AdvancedServerList not find any profile with a positive condition will it not modify the server list for the players.

/// note
It is important to note that should no [`condition`](#condition) option be set will it default to `true`, meaning that a profile with priority 1 and no condition will be chosen over a profile with priority 0 and a condition, even if said condition also returns true.
///

## Condition

The `condition` option allows you to define specific requirements that need to be met to display the profile in question.  
Not setting this option, or setting it to an empty String (`condition: ''`), will result in `true` being returned.

The condition option can hold one or multiple expressions. The Expression system is explained on the [Expressions page](expressions.md).

## Conditions :octicons-trash-24:{ style="color: #FF5252;" title="Deprecated" }

<!-- admo:deprecated Deprecated in favour of the new condition option. Will be removed in a future version. -->

The `conditions` option - not to be confused with the new [`condition`](#condition) option - allows you to define one or multiple conditions/expressions that all need to return `true` in order for the profile to be used.  
Not setting this option, or setting it to an empty list (`conditions: []`), will result in `true` being returned.

Each condition can use [placeholders](placeholders.md) to further customize it. PlaceholderAPI placeholders are **not** supported!

### Operands

<!-- admo:warning Each condition can only hold one operand -->

Each condition can use operands to compare values with each other.  
Note that if Operands are used that compare sizes (i.e. `>`) while the values are Text and not numbers will the text length be used as number.

| Operand | What it does                                                               |
|---------|----------------------------------------------------------------------------|
| `<`     | Returns true if the left value is less than the right value.               |
| `<=`    | Returns true if the left value is less than, or equal to, the right value. |
| `>`     | Returns true if the left value is more than the right value.               |
| `>=`    | Returns true if the left value is less than, or equal to, the right value. |
| `=`     | Returns true if both values are equal (Case sensitive).                    |
| `!=`    | Returns true if both values are not equal (Case sensitive).                |
| `~=`    | Returns true if both values are equal (Not case sensitive).                |
| `!~`    | Returns true if both values are not equal (Not case sensitive).            |

## Profiles

The `profile` option allows you to define a list of all other options, with the exception of [`priority`](#priority), [`condition`](#condition) or itself.

When multiple entries are defined will AdvancedServerList randomly select one.  
Should an option exist in the file itself (i.e. [`motd`](#motd)) but not in the selected profiles entry will it be used. This allows you to randomize only one aspect of a Server List Profile while keeping all other options the same.

/// details | Example
    type: example

```yaml title="YAML file"
priority: 0

profiles:
  - motd: # You can also use ['<rainbow>Line1</rainbow>','<rainbow:!>Line 2</rainbow>']
      - '<rainbow>Line 1</rainbow>'
      - '<rainbow:!>Line 2</rainbow>'
    playerCount:
      text: '<green><bold>Awesome!'
  - playerCount:
      text: '<yellow><bold>Also Awesome!'

# This MOTD is used when the second profiles entry is selected.
motd:
  - '<rainbow:2>Line A</rainbow>'
  - '<rainbow:!2>Line B</rainbow>'
```
<div class="result" markdown>

![profiles-example-1](../assets/images/examples/profiles-example-1.jpg){ loading="lazy" }  
![profiles-example-2](../assets/images/examples/profiles-example-2.jpg){ loading="lazy" }

</div>
///

## Motd

The `motd` option allows you to alter the "Message of the day" that is being displayed on a server in the multiplayer screen.  
Please see the [Formatting](formatting.md) for all supported formatting options.

Multiple, randomly selected, MOTDs can be displayed by using the [`profiles`](#profiles) option.

/// details | Example
    type: example

```yaml title="YAML file"
priority: 0

motd:
  - '<rainbow:2>Rainbow Gradient</rainbow>'
  - '<rainbow:!2>|||||||||||||||||||||||||||||||||||||</rainbow>'
```
<div class="result" markdown>

![motd-example](../assets/images/examples/motd-example.jpg){ loading="lazy" }

</div>
///

## Favicon

<!-- admo:info Images will be resized to 64x64 pixels. -->

The `favicon` option can be used to define an image to display in the server list.

The Value can be one of three possible options, each being checked for in order:

- A URL pointing to an image file. The URL needs to start with `https://` to work.
- A PNG filename, including `.png` file extension, matching a PNG file in the `favicons` folder.
- A player name or UUID, or a [placeholder](placeholders.md) that resolves into one.

Note that for the name/UUID, the site https://mc-heads.net is being used.  
Use the URL option with the `${player uuid}` placeholder if you want to use another service.
 
/// details | Example
    type: example

```yaml title="YAML file"
priority: 0

# Hides the MOTD for demonstration purposes
motd:
  - '<grey>'
  - '<grey>'

# Placeholder resolves to UUID of Andre_601
favicon: '${player uuid}'
```
<div class="result" markdown>

![favicon-example](../assets/images/examples/favicon-example.jpg){ loading="lazy" }

</div>
///

## PlayerCount

The `playerCount` option contains various other options that are used to modify the player count (The text next to the ping icon that shows the online and max players of a server).

### HidePlayers

The `playerCount -> hidePlayers` option takes a boolean (`true` or `false`) to set whether the player count should be hidden or not.

When set to true will the player count be replaced with `???`.  
In addition are the [`playerCount -> text`](#text) and [`playerCount -> hover`](#hover) options ignored.

If you want to not show any text at all, set this option to `false` (or remove it) and instead use the [`playerCount -> text`](#text) option.
 
/// details | Example
    type: example

```yaml title="YAML file"
priority: 0

# Hides the MOTD for demonstration purposes
motd:
  - '<grey>'
  - '<grey>'

playerCount:
  hidePlayers: true
```
<div class="result" markdown>

![hideplayers-example](../assets/images/examples/hideplayers-example.jpg){ loading="lazy" }

</div>
///

### Hover

The `playerCount -> hover` option allows you to define lines of text to show when a player hovers over the player count with their cursor.

Note that this option only accepts basic colors such as `<blue>` or `<grey>` and that HEX colors are not supported.
 
/// details | Example
    type: example

```yaml title="YAML file"
priority: 0

# Hides the MOTD for demonstration purposes
motd:
  - '<grey>'
  - '<grey>'

playerCount:
  hover:
    - '<grey>Line 1'
    - '<aqua>Line 2'
    - '<gold>Line 3'
```
<div class="result" markdown>

![hover-example](../assets/images/examples/hover-example.jpg){ loading="lazy" }

</div>
///

### Text

The `playerCount -> text` option allows you to define a custom text to display instead of the usual `<online>/<max>`.

Please take note of the following caveats:

- The plugin won't automatically add the `<online>/<max>` back to the text you set. Instead you'll have to use `${server playersOnline}/${server playersMax}` to recreate this.
- The override is done by using the "outdated server" text displayed when a client pings a server that is not compatible. This means that the ping icon will appear crossed out and your server will be seen as "outdated" by the client (When hovering over the ping icon). This can't be avoided nor fixed by the plugin.

If you want to not show any text, just use a single color (i.e. `text: '<grey>'`) to do so.

This option only supports basic colors and no HEX colors.
 
/// details | Example
    type: example

```yaml title="YAML file"
priority: 0

# Hides the MOTD for demonstration purposes
motd:
  - '<grey>'
  - '<grey>'

playerCount:
  text: '<yellow><bold>Cool text!'
```
<div class="result" markdown>

![text-example](../assets/images/examples/text-example.jpg){ loading="lazy" }

</div>
///

### ExtraPlayers

<!-- admo:warning Using this option overrides the output of the <code>${server playersMax}</code> placeholder (Except in condition). -->

The `playerCount -> extraPlayers` option contains settings for the "extra players" feature, which adds X to the current online player count and sets it as the server's max player count.

#### Enabled { #extraplayers-enabled }

Enables the extraPlayers feature.  
Use the [`playerCount -> extraPlayers -> amount`](#extraplayers-amount) option to set how much to add to the max players count.

#### Amount { #extraplayers-amount }

<!-- admo:info This option does nothing when <code>playerCount -> hidePlayers</code> is enabled. -->

The `playerCount -> extraPlayers -> amount` option defines how much should be added to the current online player count to then use as max player count.

Example: Setting this option to `1` while currently 10 players are online will result in `10/11` being displayed on the player count (Assuming it is not being overriden by the [`playerCount -> text`](#text) option).
 
/// details | Example
    type: example

```yaml title="YAML file"
priority: 0

# Hides the MOTD for demonstration purposes
motd:
  - '<grey>'
  - '<grey>'

playerCount:
  extraPlayers:
    enabled: true
    amount: 1
```
<div class="result" markdown>

![extraplayers-example](../assets/images/examples/extraplayers-example.jpg){ loading="lazy" }

</div>
///

### MaxPlayers

<!-- admo:info This feature is ignored should <code>playerCount -> extraPlayers</code> be enabled. -->
<!-- admo:warning Using this option overrides the output of the <code>${server playersMax}</code> placeholder (Except in condition). -->

The `playerCount -> maxPlayers` option is similar in nature to the [`playerCount -> extraPlayers`](#extraplayers) option, with the difference that the number set is being used as the max player count, instead of adding it together with the online player count first.

#### Enabled { #maxplayers-enabled }

Enables the maxPlayers feature.  
Use the [`playerCount -> maxPlayers -> amount`](#maxplayers-amount) option to set the max player count itself.

#### Amount { #maxplayers-amount }

<!-- admo:info This option does nothing when <code>playerCount -> hidePlayers</code> is enabled. -->

The `playerCount -> maxPlayers -> amount` option defines the number to use as the server's max players count.  
Unlike the [`playerCount -> extraPlayers -> amount`](#extraplayers-amount) option does this one not first add the current online player count to the number to then set.
 
/// details | Example
    type: example

```yaml title="YAML file"
priority: 0

# Hides the MOTD for demonstration purposes
motd:
  - '<grey>'
  - '<grey>'

playerCount:
  maxPlayers:
    enabled: true
    amount: -1
```
<div class="result" markdown>

![maxplayers-example](../assets/images/examples/maxplayers-example.jpg){ loading="lazy" }

</div>
///