# BanPlugins Addon

The BanPlugins Addon was made to provide placeholders for different punishment plugins to use in AdvancedServerList.

## Placeholders

### AdvancedBan

Placeholders for the plugin [AdvancedBan](https://www.spigotmc.org/resources/8695/){ target="_blank" rel="nofollow" } are available to use.

| Placeholder                     | Description                                                                               |
|---------------------------------|-------------------------------------------------------------------------------------------|
| `${advancedban isMuted}`        | Whether the player is muted or not.                                                       |
| `${advancedban muteReason}`     | The reason for why the player was muted. Returns invalid placeholder if no mute was set.  |
| `${advancedban muteDuration}`   | Relative time until the mute expires.[^1] Returns invalid placeholder if no mute was set. |
| `${advancedban muteExpiration}` | Date on when the mute will expire.[^2]                                                    |
| `${advancedban isBanned}`       | Whether the player is banned or not.                                                      |
| `${advancedban banReason}`      | The reason for why the player was banned. Returns invalid placeholder if no ban was set.  |
| `${advancedban banDuration}`    | Relative time until the ban expires.[^3] Returns invalid placeholder if no ban was set.   |
| `${advancedban banExpiration}`  | Date on when the ban will expire.[^4]                                                     |

[^1]:
    An optional boolean can be added to set whether the duration should be from the start or not. `false` is the default.  
    Example: `${advancedban muteDuration true}`
[^2]:
    An optional Date and time Pattern can be set to change the displayed output. The default is `dd, MMM yyyy HH:mm:ss`.  
    Example: `${advancedban muteExpiration yyyy-MM-dd HH:mm:ss}`
[^3]:
    An optional boolean can be added to set whether the duration should be from the start or not. `false` is the default.  
    Example: `${advancedban banDuration true}`
[^4]:
    An optional Date and time Pattern can be set to change the displayed output. The default is `dd, MMM yyyy HH:mm:ss`.  
    Example: `${advancedban banExpiration yyyy-MM-dd HH:mm:ss}`

## Example

```yaml title="banned.yml"
priority: 1
condition: '${advancedban isBanned}'

motd:
  - '<red>You were <bold>BANNED</bold>!'
  - '<red>Expires: <grey>${advancedban banExpiration}'
```