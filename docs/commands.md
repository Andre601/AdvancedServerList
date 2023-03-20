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

Shows a list of all available [subcommands](#subcommands) for AdvancedServerList.

### reload

Reloads the plugin's `config.yml`, all available YAML files in the `profiles` folder and also clears the currently cached favicons and Players.

### clearCache

Clears the currently cached favicons and Players.