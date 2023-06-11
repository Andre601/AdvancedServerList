# Placeholders

AdvancedServerList provides a set of pre-made placeholders using the `${<identifier> <placeholder>}` format adobted from BungeeTabListPlus.  
It also has built-in [PlaceholderAPI support](#placeholderapi) for Spigot and Paper Servers and since v2 can even allow you to [add your own placeholders](#custom-placeholders) by using its [API](../../api).

## Built-in Placeholders

The following placeholders are available in AdvancedServerList itself. Please note that not all placeholders are available on all platforms.

### Player Placeholders

These placeholders use the player who pinged the server, to return values. They may require the player to be cached in order to work.

| Placeholder                 | Description                                                     | Platforms    | Cached Player required?[^1] |
|-----------------------------|-----------------------------------------------------------------|--------------|-----------------------------|
| `${player name}`            | The name of the player.                                         | All          | Yes[^2]                     |
| `${player protocol}`        | The protocol version of the player.                             | All          | No                          |
| `${player uuid}`            | The UUID of the player.                                         | All          | Yes[^3]                     |
| `${player version}`         | The protocol version of the player as readable MC version.      | Velocity     | No                          |
| `${player hasPlayedBefore}` | Boolean for whether the player has played on the server before. | Spigot/Paper | Yes                         |
| `${player isBanned}`        | Boolean for whether the player has been banned from the server. | Spigot/Paper | Yes                         |
| `${player isWhitelisted}`   | Boolean for whether the player is whitelisted on the server.    | Spigot/Paper | Yes                         |

[^1]: Whether this placeholder requires the player to have joined before while AdvancedServerList was running. When `disableCache` is enabled will these placeholders not work.
[^2]: Will default to whatever name has been provided in the `unknownPlayer -> name` option of the config.yml, when the player isn't cached yet.
[^3]: Will default to whatever name has been provided in the `unknownPlayer -> uuid` option of the config.yml, when the player isn't cached yet.

### Server

These placeholders use values given by the server/proxy AdvancedServerList runs on.

| Placeholder               | Description                                                |
|---------------------------|------------------------------------------------------------|
| `${server playersOnline}` | The number of players online on this proxy/server.[^4]     |
| `${server playersMax}`    | The total number of players that can join this server.[^5] |
| `${server host}`          | The domain/IP the player pinged.[^6]                       |

[^4]:
    An optional World (For Spigot/Paper) or Server (For BungeeCord/Velocity) can be provided to display the number of players online on said world/server.  
    Full format is `${server playersOnline [world/server]}` (Replace `[world/server]` with a valid world or server name).
[^5]: This placeholder is affected by the [`Amount` option](../#amount) in a server list profile, with the exception being when used in [`Conditions`](../#conditions).
[^6]:
    An optional server name can be provided to display the IP/Domain associated with that server. Only works on BungeeCord/Velocity.
    Full format is `${server host [server]}` (Replace `[server]` with a valid server name).

## PlaceholderAPI

The Spigot and Paper version of AdvancedServerList allow the usage of *any* placeholders provided by PlaceholderAPI by using the `%<identifier>_<values>%` placeholder format.  
Depending on the placeholder used is a cached player required (i.e. `%player_name%`). AdvancedServerList will try to get a OfflinePlayer instance based on the cached UUID from the server to manage this.

!!! warning "PlaceholderAPI placeholders can NOT be used in Conditions!"

## Custom Placeholders

Since version 2 of AdvancedServerList are developers able to provide their own placeholders to be used in a server list profile.  
The format follows the same one as the [built-in placeholders](#built-in-placeholders), meaning it is `${<identifier> <placeholder>}`.

If you're a developer and want to integrate your own placeholders into AdvancedServerList, read about it [here](../../api).