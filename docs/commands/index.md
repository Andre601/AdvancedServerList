---
icon: octicons/rel-file-path-24
---

# Commands

The plugin adds commands to use for various things related to the plugin itself.  
The main command is `/advancedserverlist` but an alias called `/asl` is also registered to use.

## Permissions

The main permission is `advancedserverlist.admin` for all commands, but you can also grant access to specific commands using `advancedserverlist.command.<subcommand>` instead (i.e. `advancedserverlist.command.help` to grant access to [`/asl help`](#help)).

## Subcommands

The following subcommands are available (Subcommands are case-insensitive):

- [`help`](#help)
- [`reload`](#reload)
- [`clearCache`](#clearcache)

### help

/// info |
**Permission:** `advancedserverlist.command.help`

Shows a list of all available [subcommands](#subcommands) for AdvancedServerList.
///

### reload

/// info |
**Permission:** `advancedserverlist.command.reload`

Reloads the plugin's `config.yml` and all available YAML files in the `profiles` folder.
///

### clearCache

/// info |
**Permission:** `advancedserverlist.command.clearcache`

Clears the currently cached favicons and players.
///