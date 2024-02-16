# AdvancedBan Addon

The AdvancedBan Addon is a plugin for BungeeCord that allows you to use placeholders for the Punishment status of a player, such as if they got punished, the reason and duration.

## Placeholders

| Placeholder                   | Description                                                                                          |
|-------------------------------|------------------------------------------------------------------------------------------------------|
| `${advancedban isMuted}`      | Whether the player is muted or not.                                                                  |
| `${advancedban isBanned}`     | Whether the player is banned or not.                                                                 |
| `${advancedban muteReason}`   | The reason for the mute.                                                                             |
| `${advancedban banReason}`    | The reason for the ban.                                                                              |
| `${advancedban muteDuration}` | The duration of the mute. `true` can be added to the placeholder to get the duration from the start. |
| `${advancedban banDuration}`  | The duration of the ban. `true` can be added to the placeholder to get the duration from the start.  |

## Example

```yaml title="banned.yml"
priority: 1
condition: '${advancedban isBanned}'

motd:
  - '<red>You are <bold>BANNED'
  - '<red>Reason: ${advancedban banReason}'
```