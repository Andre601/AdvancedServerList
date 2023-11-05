---
icon: material/percent
---

# PlaceholderAPI Placeholder support

AdvancedServerList provides support for [PlaceholderAPI] placeholders to be used, while also providing its own set of placeholders.

[placeholderapi]: https://hangar.papermc.io/HelpChat/PlaceholderAPI

## Own placeholders

The below listed placeholders can be used through PlaceholderAPI in other plugins.  
Note that the values depend on the player it is used for, as AdvancedServerList will try and get a matching [Server List Profile](../profiles/index.md) to display values from.

| Placeholder                      | Description                                                                                            |
|----------------------------------|--------------------------------------------------------------------------------------------------------|
| `%asl_favicon%`                  | Gives the value of `favicon` from a matching Profile.                                                  |
| `%asl_extra_players_max%`        | Gives the value of `playerCount -> extraPlayers -> amount` from a matching Profile. Can return `null`. |
| `%asl_motd%`                     | Gives the value of `motd` in the matching Profile as a single String.                                  |
| `%asl_playercount_extraplayers%` | Gives the value of `playerCount -> extraPlayers -> amount` from a matching Profile. Can return `null`. |
| `%asl_playercount_hover%`        | Gives the value of `playerCount -> hover` from a matching Profile as a single String.                  |
| `%asl_playercount_maxplayers%`   | Gives the value of `playerCount -> maxPlayers -> amount` from a matching profile. Can return `null`.   |
| `%asl_playercount_text%`         | Gives the value of `playerCount -> text` from a matching Profile.                                      |
| `%asl_server_playersmax%`        | Gives the output of `${server playersMax}`                                                             |

## Using Placeholders in Server List profiles

You can use any PlaceholderAPI placeholder in a server list profile, if the following conditions are met:

- The placeholder does not require the player to be online.
- [PAPIProxyBridge] is installed alongside AdvancedServerList, if used on a Proxy.

To use a placeholder, simply use its default `%identifier_parameters%` format.

[papiproxybridge]: https://hangar.papermc.io/William278/PAPIProxyBridge