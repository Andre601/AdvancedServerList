# AdvancedServerList
![platform] ![tested]

AdvancedServerList is a plugin made for SpigotMC, PaperMC, BungeeCord/Waterfall and Velocity. It allows you to create server list profiles to display based on priority and conditions.

## What is a Server List Profile?
A Server List Profile refers to a YAML file located in the plugin's `profiles` directory.  
It allows you to configure specific aspects of the server display in the MC-Client's multiplayer server list, such as the MOTD, player count text, player count hover or Favicon.

> [*Read More*][profiles]

### Conditions
Thanks to conditions can you determine when a profile should be displayed in the player’s server list, to allow displaying specific text (i.e. a warning when they are using an outdated version).

When multiple profiles exist that have valid conditions will the one with the highest priority (Higher number = higher priority) be selected. Having no conditions makes the profile automatically return true.

> [*Read More*][conditions]

### Placeholders
Placeholders can be used within conditions to further customize them. They can also be used in any other text option (motd, playerCount and players) to display their respective values.
The format is `${<target> <identifier>}` which was adopted from BungeeTabListPlus. Depending on what platform you use the plugin on are only specific placeholders available.

When using the SpigotMC/PaperMC version can you also use placeholders from [PlaceholderAPI][placeholderapi] using its `%<identifier>_<values>%` placeholder format. PlaceholderAPI needs to be installed for that.

> [*Read More*][placeholders]

### Formatting
The plugin uses the MiniMessage format for a consistent, easy way of formatting your text.
Only certain options are usable for each option. As an example does motd support HEX colours and gradients, while the other options only support default colour codes.

> [*Read More*][minimessage]

## Downloads

[![dl-modrinth]][modrinth] [![dl-github]][github] [![dl-hangar]][hangar]

## Dependencies

| Dependency       | Platforms     | Required? |
| ---------------- | ------------- | --------- |
| [ProtocolLib]    | Spigot        | Yes       |
| [PlaceholderAPI] | Spigot, Paper | No        |

## Statistics

This plugin sends statistics to [bStats] to display.  
You can disable this in the global bStats config file located in `/plugins/bstats/`

- [BungeeCord][bstats-bungee]
- [Paper/Spigot][bstats-spigot]
- [Velocity][bstats-velocity]

## Screenshots

[![gallery-badge]][gallery]

<!-- Links -->
[profiles]: https://github.com/Andre601/AdvancedServerList/wiki/Profiles
[conditions]: https://github.com/Andre601/AdvancedServerList/wiki/Profiles#conditions
[placeholders]: https://github.com/Andre601/AdvancedServerList/wiki/Profiles#placeholders
[minimessage]: https://github.com/Andre601/AdvancedServerList/wiki/Profiles#minimessage

[placeholderapi]: https://www.spigotmc.org/resources/6245/

[modrinth]: https://modrinth.com/plugin/advancedserverlist
[github]: https://github.com/Andre601/AdvancedServerList/releases/latest
[hangar]: https://hangar.benndorf.dev/Andre_601/AdvancedServerList

[protocollib]: https://www.spigotmc.org/resources/1997/
[placeholderapi]: https://www.spigotmc.org/resources/6245/

[bstats]: https://bstats.org
[bstats-bungee]: https://bstats.org/plugin/bungeecord/AdvancedServerList/15585
[bstats-spigot]: https://bstats.org/plugin/bukkit/AdvancedServerList/15584
[bstats-velocity]: https://bstats.org/plugin/velocity/AdvancedServerList/15587

[gallery]: https://modrinth.com/mod/advancedserverlist/gallery

<!-- Badges -->
[platform]: https://img.shields.io/badge/Platforms-Spigot%20%7C%20Paper%20%7C%20BungeeCord%20%7C%20Velocity-blue?style=for-the-badge&logo=data:image/svg%2bxml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHZpZXdCb3g9IjAgMCAyNCAyNCIgd2lkdGg9IjI0IiBoZWlnaHQ9IjI0IiBmaWxsPSJ3aGl0ZSI+PHBhdGggZmlsbC1ydWxlPSJldmVub2RkIiBkPSJNMTEuMDYzIDEuNDU2YTEuNzUgMS43NSAwIDAxMS44NzQgMGw4LjM4MyA1LjMxNmExLjc1IDEuNzUgMCAwMTAgMi45NTZsLTguMzgzIDUuMzE2YTEuNzUgMS43NSAwIDAxLTEuODc0IDBMMi42OCA5LjcyOGExLjc1IDEuNzUgMCAwMTAtMi45NTZsOC4zODMtNS4zMTZ6bTEuMDcxIDEuMjY3YS4yNS4yNSAwIDAwLS4yNjggMEwzLjQ4MyA4LjAzOWEuMjUuMjUgMCAwMDAgLjQyMmw4LjM4MyA1LjMxNmEuMjUuMjUgMCAwMC4yNjggMGw4LjM4My01LjMxNmEuMjUuMjUgMCAwMDAtLjQyMmwtOC4zODMtNS4zMTZ6Ij48L3BhdGg+PHBhdGggZmlsbC1ydWxlPSJldmVub2RkIiBkPSJNMS44NjcgMTIuMzI0YS43NS43NSAwIDAxMS4wMzUtLjIzMmw4Ljk2NCA1LjY4NWEuMjUuMjUgMCAwMC4yNjggMGw4Ljk2NC01LjY4NWEuNzUuNzUgMCAwMS44MDQgMS4yNjdsLTguOTY1IDUuNjg1YTEuNzUgMS43NSAwIDAxLTEuODc0IDBsLTguOTY1LTUuNjg1YS43NS43NSAwIDAxLS4yMzEtMS4wMzV6Ij48L3BhdGg+PHBhdGggZmlsbC1ydWxlPSJldmVub2RkIiBkPSJNMS44NjcgMTYuMzI0YS43NS43NSAwIDAxMS4wMzUtLjIzMmw4Ljk2NCA1LjY4NWEuMjUuMjUgMCAwMC4yNjggMGw4Ljk2NC01LjY4NWEuNzUuNzUgMCAwMS44MDQgMS4yNjdsLTguOTY1IDUuNjg1YTEuNzUgMS43NSAwIDAxLTEuODc0IDBsLTguOTY1LTUuNjg1YS43NS43NSAwIDAxLS4yMzEtMS4wMzV6Ij48L3BhdGg+PC9zdmc+

[tested]: https://img.shields.io/badge/Tested%20Versions-1.19%20%7C%201.19.1%20%7C%201.19.2-blue?style=for-the-badge&logo=image/svg%2bxml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHZpZXdCb3g9IjAgMCAyNCAyNCIgd2lkdGg9IjI0IiBoZWlnaHQ9IjI0IiBmaWxsPSJ3aGl0ZSI+PHBhdGggZD0iTTE3LjI4IDkuMjhhLjc1Ljc1IDAgMDAtMS4wNi0xLjA2bC01Ljk3IDUuOTctMi40Ny0yLjQ3YS43NS43NSAwIDAwLTEuMDYgMS4wNmwzIDNhLjc1Ljc1IDAgMDAxLjA2IDBsNi41LTYuNXoiPjwvcGF0aD48cGF0aCBmaWxsLXJ1bGU9ImV2ZW5vZGQiIGQ9Ik0zLjc1IDJBMS43NSAxLjc1IDAgMDAyIDMuNzV2MTYuNWMwIC45NjYuNzg0IDEuNzUgMS43NSAxLjc1aDE2LjVBMS43NSAxLjc1IDAgMDAyMiAyMC4yNVYzLjc1QTEuNzUgMS43NSAwIDAwMjAuMjUgMkgzLjc1ek0zLjUgMy43NWEuMjUuMjUgMCAwMS4yNS0uMjVoMTYuNWEuMjUuMjUgMCAwMS4yNS4yNXYxNi41YS4yNS4yNSAwIDAxLS4yNS4yNUgzLjc1YS4yNS4yNSAwIDAxLS4yNS0uMjVWMy43NXoiPjwvcGF0aD48L3N2Zz4=

[dl-modrinth]: https://raw.githubusercontent.com/intergrav/devins-badges/main/badges/modrinth_vector.svg

[gallery-badge]: https://raw.githubusercontent.com/intergrav/devins-badges/main/badges/modrinth-gallery_vector.svg
