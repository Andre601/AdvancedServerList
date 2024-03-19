---
icon: octicons/milestone-24
---

# Getting Started

This page will explain how to set up AdvancedServerList and make it work for your server.  
It tries to be as easy to understand as possible. But if you still have problems, consider joining the [Discord Server](https://discord.gg/6dazXp6) for help.

## 1. Download the plugin { #download-the-plugin }

The first step would be to download the plugin from a download page of your choice.  
AdvancedServerList is available on the following places (Please note the current status!):

<div class="grid cards" markdown>

-   [:simple-modrinth: **Modrinth**][modrinth]
    
    ----
    
    :octicons-check-24:{ style="color: var(--md-badge-fg--success);" } **Actively updated!**

-   [:simple-codeberg: **Codeberg**][codeberg]
    
    ----
    
    :octicons-check-24:{ style="color: var(--md-badge-fg--success);" } **Actively updated!**

-   [:fontawesome-solid-paper-plane: **HangarMC**][hangar]
    
    ----
    
    :octicons-check-24:{ style="color: var(--md-badge-fg--success);" } **Actively updated!**


-   [:simple-spigotmc: **SpigotMC**][spigot]
    
    ----
    
    :octicons-alert-24:{ style="color: var(--md-badge-fg--warning);" } **Discontinued!**

</div>

[modrinth]: https://modrinth.com/plugin/advancedserverlist
[codeberg]: https://codeberg.org/Andre601/AdvancedServerList
[spigot]: https://www.spigotmc.org/resources/102910/
[hangar]: https://hangar.papermc.io/Andre_601/AdvancedServerList

The plugin is available for Paper, BungeeCord, Waterfall and Velocity, and has been tested on these platforms. It may support additional forks, but this is not guaranteed.

The plugin also supports these additional plugins. They are all optional.

<div class="grid cards" markdown>

-   [**PlaceholderAPI**][placeholderapi]
    
    ----
    
    AdvancedServerList supports the use of placeholder from PlaceholderAPI in all its text options, except for the `condition` option.
    
    ----
    
    **Supported on:**  
    :fontawesome-solid-paper-plane: Paper

-   [**ViaVersion**][viaversion]
    
    ----
    
    When installed will AdvancedServerList use ViaVersion to resolve the protocol version of a player for its PlaceholderAPI placeholders.
    
    ----

    **Supported on:**  
    :fontawesome-solid-paper-plane: Paper

-   [**PAPIProxyBridge**][papiproxybridge]
    
    ----
    
    Allows AdvancedServerList to parse placeholders from PlaceholderAPI while being used on a proxy.
    
    ----

    **Supported on:**  
    :simple-spigotmc: BungeeCord, :fontawesome-solid-paper-plane: Velocity

-   [**Maintenance**][maintenance]
    
    ----
    
    AdvancedServerList will disable any modification of the server list entry should Maintenance mode be enabled.
    
    ----

    **Supported on:**  
    :fontawesome-solid-paper-plane: Paper, :simple-spigotmc: BungeeCord, :fontawesome-solid-paper-plane: Velocity

-   [**BanPlugins Addon**][addon]
    
    ----
    
    Official Add-on for AdvancedServerList that adds placeholders for different Ban/Punishment plugins such as AdvancedBan and LibertyBans.
    
    ----

    **Supported on:**  
    :fontawesome-solid-paper-plane: Paper, :simple-spigotmc: BungeeCord, :fontawesome-solid-paper-plane: Velocity

</div>

[placeholderapi]: https://hangar.papermc.io/HelpChat/PlaceholderAPI
[viaversion]: https://hangar.papermc.io/ViaVersion/ViaVersion
[papiproxybridge]: https://hangar.papermc.io/William278/PAPIProxyBridge
[maintenance]: https://hangar.papermc.io/kennytv/Maintenance
[addon]: ../addons/banplugins.md

## 2. Installation { #installation }

Installing the plugin is as simple as moving the jar file into the plugins folder. Just make sure to download the right version of AdvancedServerList for the right platform.  
Here is a quick table showing the jar file name with the platforms it is made for:

| Jar file name                                 | Platforms             |
|-----------------------------------------------|-----------------------|
| `AdvancedServerList-Paper-{version}.jar`      | Paper                 |
| `AdvancedServerList-BungeeCord-{version}.jar` | BungeeCord, Waterfall |
| `AdvancedServerList-Velocity-{version}.jar`   | Velocity              |

After you added it to your plugins folder - alongside any other dependency you may want - (re)start your server or proxy to enable the plugin.  
If everything goes well should AdvancedServerList create a folder named `AdvancedServerList` (`advancedserverlist` on Velocity) with additional files and folders inside of it.

### Folder structure

When you install AdvancedServerList for the first time should it have created the following file structure inside the plugins folder of your Server or Proxy:
```
AdvancedServerList/
├── favicons/
├── profiles/
│   └── default.yml
└── config.yml
```
Let's cover the files and folders...

#### Favicons folder

This folder allows you to store PNG images that should be used as favicons by the plugin. A favicon from this folder can be used by referencing the name (Including the `.png` file extension) in the [`favicon` option of the server list profile](../profiles/index.md#favicon).

#### Profiles folder and default.yml { #profiles-folder-and-default-yml }

The profiles folder is home of the server list profile files which allow you to modify the server list entry of your server in a player's multiplayer screen.  
On first creation will the folder have a `default.yml` file. This file contains all the options available to use alongside comments to try and help you in using it properly.

/// details | default.yml content
    type: file

```yaml
#
# Set the priority of this profile.
# If there are multiple profiles (files) will they be sorted by priority, starting with highest.
#
# Read more: https://asl.andre601.ch/profiles/#priority
#
priority: 0

#
# Set condition(s) that should return true to display this profile.
# This allows you to create profiles to only show for specific scenarios (i.e. player is banned).
#
# Multiple conditions can be chaing with the keywords 'and', 'or', or their counterparts '&&' and '||'.
#
# No condition or an empty condition will be treated as true by default.
#
# Read more: https://asl.andre601.ch/profiles/#condition
#
condition: ''

#
# Set one or multiple profiles.
#
# A profile can contain the same options as used in the file itself, except for priority and condition.
# If multiple entries are present, a random one will be selected. Any options set in the selected profile will override
# any existing option in the file.
#
# Example: The below profile configuration will randomly change the MOTD between two types, but keep the Hover text
#          the same.
#
# When not present or empty (profiles: []), no profiles will be used and global options from the file will be used instead.
#
# Read more: https://asl.andre601.ch/profiles/#profiles
#
profiles:
  - motd: ['<aqua>Line A', '<gold>Line B']
  - motd:
      - '<aqua>Line 1'
      - '<gold>Line 2'

#
# Set the "Message of the day" to display.
# This option supports RGB colors for 1.16+ Servers and clients, including gradients using MiniMessage's <gradient>
# option.
#
# Only the first two lines will be considered and any additional one is ignored.
#
# When not present or empty (motd: []), no MOTD will be set.
#
# Read more: https://asl.andre601.ch/profiles/#motd
#
motd:
  - '<grey>First Line'
  - '<grey>Second Line'

#
# Set the Favicon (image on the left of a server entry) to display.
# This option supports three types of inputs:
#
#   1. Name, including .png extension, pointing to an image located in the plugin's favicons folder.
#   2. URL pointing to an image.*
#   3. Player name, UUID or placeholders returning one of these.*
#      https://mc-heads.net will be used to retrieve an image of the provided name/UUID.
#
# * These images are loaded asynchronously, meaning they may not yet be "ready" when AdvancedServerList is done with
#   the event, requiring a refresh of the server list by the player to have it shown.
#
# Any images above or below 64x64 pixels will be automatically resized to these dimensions.
#
# If not present or empty (favicon: ''), defaults to not modifying the favicon.
#
# Read more: https://asl.andre601.ch/profiles/#favicon
#
favicon: ''

#
# Contains options for modifying the player count (Text displaying <online>/<max>).
#
# Read more: https://asl.andre601.ch/profiles/#playercount
#
playerCount:
  #
  # Set whether the Player count should be hidden.
  # When set to true, the player count will be displayed as '???' instead of the usual '<online>/<max>' and all other
  # playerCount options, except for extraPlayers and maxPlayers, will be ignored.
  #
  # If not present, defaults to false.
  #
  # Read more: https://asl.andre601.ch/profiles/#hideplayers
  #
  hidePlayers: false
  #
  # Set the lines shown when the player hovers over the player count.
  # Unlike the MOTD option does this one only support basic color and formatting codes. Any RGB colors will be
  # downsampled to the nearest matching color.
  #
  # If not present or empty (hover: []), defaults to not modifying the hover.
  #
  # Read more: https://asl.andre601.ch/profiles/#hover
  #
  hover:
    - '<aqua>Line 1'
    - '<yellow>Line 2'
    - '<green>Line 3'
  #
  # Set the text that should be displayed instead of the default '<online>/<max>' text.
  #
  # Using this option will show the server as "outdated" to the client (Have the ping icon crossed out).
  # This cannot be fixed and would require Mojang to change stuff.
  #
  # If not present or empty (text: ''), defaults to not modifying the text.
  #
  # Read more: https://asl.andre601.ch/profiles/#text
  #
  text: ''
  #
  # The extraPlayers feature allows to set a number that should be added to the current online players and then be
  # used as the "max" player count.
  #
  # Using this feature will modify the ${server playersMax} placeholder, except for usage in the condition option.
  #
  # Read more: https://asl.andre601.ch/profiles/#extraplayers
  #
  extraPlayers:
    #
    # Set whether the extraPlayers feature should be enabled or not.
    # When set to true, the max players count will be modified to be the number of online players with the 'amount'
    # value added to it.
    #
    # If not present, defaults to false.
    #
    # Read more: https://asl.andre601.ch/profiles/#extraplayers-enabled
    #
    enabled: false
    #
    # Set the number that should be added to the online player count to then use as the new max player count.
    #
    # Example: Setting this to 1 will result in ${server playersMax} displaying 11 while 10 players are online,
    #          9 when 8 are online and so on.
    #
    # If not present, defaults to 0.
    #
    # Read more: https://asl.andre601.ch/profiles/#extraplayers-amount
    #
    amount: 0
  #
  # The maxPlayers feature allows to set a number to use as the max player count to display.
  #
  # Using this feature will modify the ${server playersMax} placeholder, except for usage in the condition option.
  #
  # Read more: https://asl.andre601.ch/profiles/#maxplayers
  #
  maxPlayers:
    #
    # Set whether the maxPlayers feature should be enabled or not.
    # When set to true, the max player count will be set to the 'amount' value.
    #
    # If not present, defaults to false.
    #
    # Read more: https://asl.andre601.ch/profiles/#maxplayers-enabled
    #
    enabled: false
    #
    # Set the number that should be used as the new max players count.
    #
    # If not present, defaults to 0.
    #
    # Read more: https://asl.andre601.ch/profiles/#maxplayers-amount
    #
    amount: 0
```
///

#### Config.yml

The config.yml file contains settings related to the plugin itself and some of its functionality.  
These functions include, but aren't limited to:

- Setting the default name and UUID used for an unknown (Not cached) player.
- Disabling/Enabling caching of players.
- Disabling/Enabling checking for updates.
- Disabling/Enabling debug mode.
- Setting how long favicons should be cached for.
- The config version used for migration. Don't touch this, or it may reset your config.

Similar to the default.yml is this file containing comments to try and explain the settings in questions.  
Do note that if the config gets migrated from an older version, that the comments will be discarded in the process, unfortunately.

/// details | default config.yml content
    type: file

```yaml
#
# Used for when AdvancedServerList doesn't have a cached player to use for player placeholders.
#
unknownPlayer:
  #
  # The name to use. Defaults to Anonymous.
  #
  name: "Anonymous"
  #
  # The UUID to use. Defaults to 606e2ff0-ed77-4842-9d6c-e1d3321c7838 (UUID of MHF_Question).
  #
  uuid: "606e2ff0-ed77-4842-9d6c-e1d3321c7838"

#
# Should the Caching of players be disabled?
#
# When set to true will AdvancedServerList not load any players from the cache.data file (if present) nor save any to it.
# Placeholders that rely on the player being cached (such as ${player name}) will not work while this option is enabled.
#
# Defaults to false when not present.
#
disableCache: false

#
# Should AdvancedServerList check for new versions?
#
# When set to true will AdvancedServerList check for a new version on modrinth.com every 12 hours and inform you in the
# console about whether it found any new update or not.
#
# Defaults to false when not present.
#
checkUpdates: true

#
# Should debug mode be enabled?
#
# When set to true will AdvancedServerList print additional messages to the console prefixed with "[DEBUG]".
# It is recommended to only enable this when having problems or when being told to by the plugin dev, as it could
# otherwise cause significant spam in your console.
#
# Defaults to false when not present.
#
debug: false

#
# Sets the time in minutes for favicons to be cached.
# The number cannot be less than 1.
#
# Defaults to 1 if not set.
#
faviconCacheTime: 1

#
# List of options that should be disabled while the Maintenance Mode of the plugin Maintenance is active.
# Any option not listed here will NOT be disabled, meaning AdvancedServerList will continue to handle it.
#
# Each option defaults to true when not present, disabling said option while Maintenance is active.
#
disableDuringMaintenance:
  #
  # When true (Default), disables handling of MOTD while Maintenance is enabled.
  #
  motd: true
  #
  # When true (Default), disables handling of Favicon while Maintenance is enabled.
  #
  favicon: true
  #
  # When true (Default), disables hiding of player count while Maintenance is enabled.
  #
  hidePlayers: true
  #
  # When true (Default), disables handling of player count text while Maintenance is enabled.
  #
  playerCountText: true
  #
  # When true (Default), disables handling of player count hover while Maintenance is enabled.
  #
  playerCountHover: true
  #
  # When true (Default), disables use of extra players feature while Maintenance is enabled.
  #
  extraPlayers: true
  #
  # When true (Default), disables use of max players feature while Maintenance is enabled.
  #
  maxPlayers: true

#
# DO NOT EDIT!
#
# This is used internally to determine if the config needs to be migrated.
# Changing or even removing this option could result in your config being broken.
#
config-version: 5
```
///

#### Additionally created files and folders

There are certain files and folders that get created when certain events happen:

- A `playercache.json` file will be created on plugin shutdown containing a collection of IPs, UUIDs and Player names for every player that joined the server while AdvancedServerList was running.  
    This file is used to identify a player through their last used IP, to replace placeholders such as `${player name}` or `${player uuid}` with their respective values using the cached data.  
    This file is not created, used nor updated when `disableCache` is set to `true` in the config.yml
- A `backups` folder containing old config.yml files.  
    This folder is created when AdvancedServerList migrates your old config to a new version. This allows you to transfer settings over in case the migration didn't work properly.  
    Each backup will have the name pattern `config-<date>.yml` with `<date>` being the date and time of when this file has been created.

## 3. Creating your first profile { #creating-your-first-profile }

Creating your first profile is relatively simple.

It's best to open the `default.yml` file located inside `profiles` with the file editor of your choice ([:simple-vscodium: VSCodeium](https://vscodium.com/) or [:simple-notepadplusplus: Notepad++](https://notepad-plus-plus.org/) are recommended).  
This file should contain all available options that you can set and alter.

Edit the options to whatever you like. All text-based options, except [`condition`](../profiles/index.md#condition), support [MiniMessage formatting](../profiles/formatting.md).  
The [`motd`](../profiles/index.md#motd) option also supports Hexadecimal colors, assuming the server/proxy runs at least MC 1.16.

If you're unsure how a specific option should look like, head over to the [Profiles page](../profiles/index.md) for more information about the general structure.  
All you need to know is, that a bare-bones server list profile requires a valid [priority](../profiles/index.md#priority) and at least one of the settings to be present.

To give an example is here a basic profile that only modifies the MOTD and nothing else:
```yaml title="default.yml"
priority: 0

motd:
  - 'Hello'
  - 'World!'
```

### Additional profiles

To add additional profiles, create a new YAML file inside the `profiles` folder. The name doesn't matter, but it is recommended to keep it lowercase, use alphanumeric characters (`a-z, 0-9`), hyphens (`-`), underscores (`_`) and avoid spaces. You should also name it after what it is for, to have it more easily organized.

Inside the file, add a priority and at least one setting, similar to the above shown example.  
This would already be enough to have a valid profile, but depending on the priority and file name may the file not be used, or used over other profiles.

If you want to only show this file under specific situations can you use the [`condition`](../profiles/index.md#condition) option to apply conditions that need to be met to show the profile.  
The condition option would have a single String containing one or muliple [Expressions](../profiles/expressions.md). Only if the condition would return true, would the profile be displayed, granted that it has a higher priority than any other valid file with a true condition output.

/// info | Note
Profiles which do not have any conditions, or have an empty condition, will always be considered to have a true condition, meaning that a profile with higher priority and no condition will be selected over a profile with lower priority and a condition, even if said condition returns true.
///

Here is another example profile using conditions to show it when the player was banned on the server:
```yaml title="other_profile.yml"
priority: 1

condition: '${player isBanned}'

motd:
  - '<red>You got <bold>banned</bold> from this server.'
```

### Further examples

The [Examples page](../examples/index.md) contains more examples of different server list profiles for all kinds of situations.

## 4. Loading profiles { #loading-profiles }

Once you've set up your server list profile(s) is it time to load it/them.  
To do this, simply run `/asl reload` as player (Requires permission `advancedserverlist.admin` or `advancedserverlist.command.reload`) or `asl reload` through the Server/Proxy console.

The plugin should then load any valid YAML file inside the `profiles` folder to then use.

And that's already it! You now have a working server list profile for your server.