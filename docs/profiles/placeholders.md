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

[^1]:
    Whether this placeholder requires the player to have joined before while AdvancedServerList was running.  
    When `disableCache` is enabled will these placeholders not work.
[^2]: Will default to whatever name has been provided in the `unknownPlayer -> name` option of the config.yml, when the player isn't cached yet.
[^3]: Will default to whatever name has been provided in the `unknownPlayer -> uuid` option of the config.yml, when the player isn't cached yet.

### Server

These placeholders use values given by the server/proxy AdvancedServerList runs on.

| Placeholder                  | Description                                                             |
|------------------------------|-------------------------------------------------------------------------|
| `${server playersOnline}`    | The number of players online on this proxy/server.[^4]                  |
| `${server playersMax}`       | The total number of players that can join this server.[^5]              |
| `${server host}`             | The domain/IP the player pinged.[^6]                                    |
| `${server whitelistEnabled}` | Whether the whitelist is enabled or not. Only available on Spigot/Paper |

[^4]:
    An space-separated list of worlds (Spigot/Paper) or Servers (BungeeCord/Velocity) can be provided to display the number of players in these worlds/servers.  
    Example: `${server playersOnline lobby1 lobby2}` will display the numbers of players online on the servers `lobby1` and `lobby2`.
[^5]:
    When the [`amount` option](../#amount) is used will this placeholder display the modified max players count.  
    The only exception is [`conditions`](../#conditions) where it uses the actual max players of the server/proxy.
[^6]:
    An optional server name can be provided to display the IP/Domain associated with that server. Only works on BungeeCord/Velocity.
    Example: `${server host survival}` would display the IP/Domain associated with the `survival` server.

## PlaceholderAPI

You can use Placeholders provided by PlaceholderAPI within a Server List profile using the default `%<identifier>_<values>%` pattern provided by PlaceholderAPI.  
Support for PlaceholderAPI is given for all platforms!

!!! note "Current limitations"
    There are some limitations, namely:

    - Placeholders requiring the player to be online will not work. Other placeholders may require an offline player instance, which AdvancedServerList can provide using its cached player mechanic.
    - The BungeeCord and Velocity version of AdvancedServerList require PAPIProxyBridge for proper PlaceholderAPI support. Make sure to use at least 1.3 of the plugin.
    - Placeholders from PlaceholderAPI can **not** be used within conditions!

## Custom Placeholders

Since version 2 of AdvancedServerList are developers able to provide their own placeholders to be used in a server list profile.  
The format follows the same one as the [built-in placeholders](#built-in-placeholders), meaning it is `${<identifier> <placeholder>}`.

If you're a developer and want to integrate your own placeholders into AdvancedServerList, read about it [here](../../api).
