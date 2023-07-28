## About

!!! warning "The below examples may only work in v1.10.0 or newer!"

This page lists some examples you may want to use for your proxy or server.

Feel free to suggest additional examples on my [Discord Server](https://discord.gg/6dazXp6) in the `#advancedserverlist` channel.

## Table of Contents

- [Versions](#versions)
    - [Different List for outdated Clients](#different-list-for-outdated-clients)
    - [Different MOTD for < 1.16](#different-motd-for-116)
- [Spigot/PaperMC](#spigotpapermc)
    - [Different profile for banned Player](#different-profile-for-banned-player)
- [Other](#other)
    - [Different profile for different host](#different-profile-for-different-host)
    - [Different profile for known player](#different-profile-for-known-player)
    - [Random MOTD but same hover](#random-motd-but-same-hover)

----
## Versions

Some examples about version-related things.

### Different List for outdated Clients

You may want to display a separate MOTD or text for incompatible client versions.  
This example here shows a separate MOTD and player count message for when the player's client version is older than what the server supports.

```yaml title="outdated-client.yml"
priority: 1

# Display this profile, if the version of the player is below 760 (1.19.1)
conditions:
  - '${player protocol} < 760'

motd:
  - <red>You are using an outdated client!
  - <red>Please update to 1.19.1 to join.

playerCount:
  text: '<red>Outdated Client!'
```

### Different MOTD for < 1.16

This profile would display a MOTD without HEX colours if the player's version is below 735 (1.16).

```yaml title="pre-1_16.yml"
priority: 1

# Display this profile, if the version of the player is below 735 (1.16)
conditions:
  - '${player protocol} < 735'

motd:
  - <grey>Welcome to <aqua>YourServer.com</aqua>!
  - <grey>Enjoy your stay.
```

----
## Spigot/PaperMC

Examples that can be used with the Spigot or PaperMC version.

### Different profile for banned Player

!!! warning "Requires a cached player to work reliably."

Displays a separate profile for when the player has been banned from the server.

```yaml title="banned.yml"
priority: 1

conditions:
  - '${player isBanned} = true'

motd:
  - <red>You have been <bold>BANNED</bold>!
  - <red>Go away.

# Don't show the player count to the banned player.
playerCount:
  hidePlayers: true
```

----
## Other

Some misc examples.

### Different profile for different host

Display a different profile when the playing pings a specific domain/IP.

```yaml title="specific-host.yml"
priority: 1

conditions:
  - '${server host} = other.example.com'

motd:
  - <grey>Please use <aqua>mc.example.com
  - <grey>to join our server.
```

### Different profile for known player

Display a different profile, if the player pinging the server is "known" by AdvancedServerList (Has joined before with the same IP).

```yaml title="personalized.yml"
priority: 1

conditions:
  - `${player name} != Anonymous` # "Anonymous" should be replaced with whatever you use in 'unknown_player'

motd:
  - <grey>Hello there <aqua>${player name}</aqua>!
  - <grey>It's nice to see you again.

playerCount:
  hover:
    - '<grey>Welcome back <aqua>${player name}</aqua>!'
    - '<grey>'
    - '<grey>Come and join <aqua>${server playersOnline}'
    - '<aqua>other players</aqua> <grey>on the Server!'
```

### Random MOTD but same hover

v1.10.0 of AdvancedServerList introduced an option called `profiles`. It allows you to create multiple variants of an MOTD, playercount, etc. while also supporting the normal (global) options as a fallback.

The following example will create a profile that has a randomized MOTD, but the same hover for both:

```yaml title="random-motd.yml"
priority: 1

profiles:
  - motd:
      - 'A randomized MOTD.'
      - 'Lines change, but hover is the same.'
  - motd: ['Different Syntax.', 'May be more readable.']

playerCount:
  hover:
    - 'This player count hover always'
    - 'stays the same, no matter what'
    - 'MOTD is being used here.'
```
