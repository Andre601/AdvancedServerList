---
icon: material/percent
---

# PlaceholderAPI Placeholder support

AdvancedServerList provides support for [PlaceholderAPI] placeholders to be used, while also providing its own set of placeholders.

[placeholderapi]: https://www.spigotmc.org/resources/6245/

## Own placeholders

The following list of placeholders is available for use through PlaceholderAPI.  
Please note that they are only available through the Bukkit version of AdvancedServerList.

Also note, that AdvancedServerList will try to find and use a [Server List profile](../profiles/index.md) that has maching conditions for the player, before returning any placeholder values.  
This means that the output of a placeholder depends on what profile (if any) matches for the player.

| Placeholder               | Description                                                                   |
|---------------------------|-------------------------------------------------------------------------------|
| `%asl_favicon%`           | Gives the value of `favicon` from a matching Profile.                         |
| `%asl_extra_players_max%` | Gives the value of `playerCount.extraPlayers.amount` from a matching Profile. |
| `%asl_motd%`              | Gives the value of `motd` in the matching Profile.                            |
| `%asl_playercount_hover%` | Gives the value of `playerCount.hover` from a matching Profile.               |
| `%asl_playercount_text%`  | Gives the value of `playerCount.text` from a matching Profile.                |

## Using Placeholders in Server List profiles

You can use any PlaceholderAPI placeholder in a server list profile, if the following conditions are met:

- The placeholder does not require the player to be online.
- [PAPIProxyBridge] is installed alongside AdvancedServerList, if used on a Proxy.

To use a placeholder, simply use its default `%identifier_parameters%` format.

[papiproxybridge]: https://hangar.papermc.io/William278/PAPIProxyBridge