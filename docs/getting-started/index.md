---
icon: octicons/milestone-24
---

# Getting Started

This page will explain how to set up AdvancedServerList and make it work for your server.  
It tries to be an easy to understand as possible. But if you still have problems, consider joining the [Discord Server](https://discord.gg/6dazXp6) for help.

## 1. Download the plugin { #download-the-plugin }

The first step would be to download the plugin from a download page of your choice. As of writing this is AdvancedServerList updated and available on these sites:

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
    
    Supported on:
    
    - :fontawesome-solid-paper-plane: Paper

-   [**ViaVersion**][viaversion]
    
    ----
    
    Supported on:
    
    - :fontawesome-solid-paper-plane: Paper

-   [**PAPIProxyBridge**][papiproxybridge]
    
    ----
    
    Supported on:
    
    - :simple-spigotmc: BungeeCord
    - :fontawesome-solid-paper-plane: Velocity

-   [**Maintenance**][maintenance]
    
    ----
    
    Supported on:
    
    - :fontawesome-solid-paper-plane: Paper
    - :simple-spigotmc: BungeeCord
    - :fontawesome-solid-paper-plane: Velocity
    
</div>

[placeholderapi]: https://hangar.papermc.io/HelpChat/PlaceholderAPI
[viaversion]: https://hangar.papermc.io/ViaVersion/ViaVersion
[papiproxybridge]: https://hangar.papermc.io/William278/PAPIProxyBridge
[maintenance]: https://hangar.papermc.io/kennytv/Maintenance

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
```yaml
#
# Set the priority of this profile.
# Higher number = Higher priority.
#
# Read more: https://asl.andre601.ch/profiles/#priority
#
priority: 0

#
# Set conditions that need to me met to show this profile.
# You can use keywords 'and' or 'or' (Or alternatively '&&' or `||`) to make more complex condition sets.
#
# Read more: https://asl.andre601.ch/profiles/#condition
#
# condition: '${player protocol} < 759'

#
# Option that allows you to define multiple profiles to use.
# Each entry supports the same options as the server list profile does. Whenever an option is not present, or set to
# an empty value (If supported) would the option in the file be used (if present).
# This allows you to f.e. randomize the MOTD while keeping other options the same, without copy-pasting the same options.
#
# A profile will be selected randomly.
#
# Remove this option or set it to profiles: [] to use the global options of this file.
#
# Read more: https://asl.andre601.ch/profiles/#profiles
#
#profiles:
#  - motd:
#      - '<aqua>Line 1'
#      - '<aqua>Line 2'
#    favicon: ''
#    playerCount:
#      hidePlayers: false
#      hover:
#        - '<green>Line 1'
#        - '<green>Line 2'
#        - '<green>Line 3'
#      text: ''
#      extraPlayers:
#        enabled: false
#        amount: 0

#
# Set the default (Global) MOTD used by this server list profile.
# Supports HEX colors for 1.16+ servers including gradients using MiniMessage's <gradient:color:color> format.
# 
# Remove this option or set it to motd: [] to not alter the MOTD.
#
# Read more: https://asl.andre601.ch/profiles/#motd
#
motd:
  - '<grey>Line 1'
  - '<grey>Line 2'

#
# Allows you to set a global (Default) favicon for this server list profile.
# You can use one of three possible options:
#   1. A file name matching a PNG file located in the 'favicons' folder (Needs to end with .png)
#   2. A URL pointing to a valid Image
#   3. ${player name} to display the player's head as favicon. Uses https://mc-heads.net for this
#
# Remove this option or set to favicon: '' to not replace the Favicon.
#
# Read more: https://asl.andre601.ch/profiles/#favicon
#
favicon: ''

#
# Contains various options to manipulate the player count (Text displaying online and max players allowed).
#
# Read more: https://asl.andre601.ch/profiles/#playercount
#
playerCount:
  #
  # Whether AdvancedServerList should hide the player count or not.
  # When set to true will the player count display '???' instead of <online>/<max>
  #
  # Note that when this is enabled will all other options, except for 'extraPlayers', be ignored.
  #
  # Defaults to false when not present.
  #
  # Read more: https://asl.andre601.ch/profiles/#hideplayers
  #
  hidePlayers: false
  #
  # Sets the text displayed when hovering over the player count.
  #
  # Remove this option or set it to hover: [] to not modify the player samples on the player count.
  #
  # Read more: https://asl.andre601.ch/profiles/#hover
  #
  hover:
    - '<grey>Line 1'
    - '<grey>Line 2'
    - '<grey>Line 3'
  #
  # Modifies the text that usually displays '<online>/<max>'
  # 
  # Note that when used, the Ping icon will display as 'outdated server'/'outdated client'.
  # This is nothing that can be fixed or avoided and would require changes from Mojang.
  #
  # Remove this option or set it to text: '' to not modify the player count text.
  #
  # Read more: https://asl.andre601.ch/profiles/#text
  #
  text: ''
  #
  # Allows you to modify the max player count displayed.
  # Note that this will affect ${server playersMax} (Except for when used in conditions).
  #
  # Read more: https://asl.andre601.ch/profiles/#extraplayers
  #
  extraPlayers:
    #
    # Enables/Disables the extraPlayers option.
    # When enabled will the total amount of players allowed to join be modified to the current
    # number of online players + 'amount'
    #
    # Defaults to false when not present.
    #
    # Read more: https://asl.andre601.ch/profiles/#extraplayers-enabled
    #
    enabled: false
    #
    # Sets how much should be added/subtracted from the current number of online players
    # to then use for the max player count.
    #
    # Example:
    # When set to 1 while 10 players are online would the player count - if unmodified -
    # display '10/11'. When -1 is used will it display '10/9'
    #
    # Defaults to 0 when not present.
    #
    # Read more: https://asl.andre601.ch/profiles/#extraplayers-amount
    #
    amount: 0
  #
  # Allows you to set the max players that could join your server.
  # Note that this option influences the '${server playersMax}' placeholder.
  #
  # The settings of this option have no effect should 'extraPlayers' be enabled
  #
  # Read more: https://asl.andre601.ch/profiles/#maxplayers
  #
  maxPlayers:
    #
    # Enables/Disables the maxPlayers option.
    # When enabled will the total amount of players allowed to join be changed to what 'amount' is set to.
    #
    # Defaults to false when not present.
    # 
    # Read more: https://asl.andre601.ch/profiles/#maxplayers-enabled
    #
    enabled: false
    #
    # Sets the number to display for the max number of players allowed to join your server.
    #
    # Example:
    # Setting this option to 0 (Default) while 1 player is online would display '1/0'
    #
    # Defaults to 0 if not set.
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
- The config version used for migration. Don't touch this, or it may reset your config.

Similar to the default.yml is this file containing comments to try and explain the settings in questions.  
Do note that if the config gets migrated from an older version, that the comments will be discarded in the process, unfortunately.

/// details | default config.yml content
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
# DO NOT EDIT!
#
# This is used internally to determine if the config needs to be migrated.
# Changing or even removing this option could result in your config being broken.
#
config-version: 2
```
///

#### Additionally created files and folders

There are certain files and folders that get created when certain events happen:

- A `playercache.json` file will be created on plugin shutdown containing a list of IPs, UUIDs and Player names for every player that joined the server while AdvancedServerList was running.  
  This file allows to replace `${player name}` and `${player uuid}` with their name or UUID respectively, while they are not on the server, by using the IP they used to join with.  
  This file is not created nor updated when `disableCache` is set to `true` in the config.yml
- A `backups` folder containing old config.yml files.  
  This folder is created when AdvancedServerList migrates your old config to a new version. This allows you to transfer settings over in case the migration didn't work properly.

## 3. Creating your first profile { #creating-your-first-profile }

Creating your first profile is relatively simple.

To start, open the `default.yml` file located inside the `profiles` folder using a file editor of your choice (VSCode or Notepad++ are recommended). It should contain all the available options for a server list profile.  
Next can you edit the options available to whatever you like. All text options (Except [conditions](../profiles/index.md#conditions)) support [MiniMessage formatting](../profiles/formatting.md).

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

To add additional profiles, create a new YAML file inside the `profiles` folder. The name doesn't matter, but it is recommended to keep it lowercase, use alphanumeric characters (`a-z, 0-9`), hyphens (`-`), underscores (`_`) and avoid spaces.

Inside the file, add a priority and at least one setting, similar to the above shown example.  
This would already be enough and AdvancedServerList would use it. If you however want to only show it when certain conditions are met, should you set the priority to a higher value and add conditions.

Conditions can be multiple strings containing [operands](../profiles/index.md#operands) to evaluate them. As an example `${server playersOnline} >= 10` would return true if the current number of online players is larger or equal to 10.

Here is another example profile using conditions:
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
To do this, simply run `/asl reload` as player (Requires permission `advancedserverlist.admin` or `advancedserverlist.command.reload`) or through the Server/Proxy console.

The plugin should then load any valid YAML file inside the `profiles` folder to then use.

And that's already it! You now have a working server list profile for your server.