# AdvancedServerList
<a href="https://modrinth.com/plugin/advancedserverlist" target="_blank">
  <img src="https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@2/assets/cozy/available/modrinth_vector.svg" height="64" align="right" alt="modrinth" title="Available on Modrinth">
</a>

AdvancedServerList is a plugin that allowys you to customize your server's MOTD, favicon, Player count text and player count hover displayed in a player's multiplayer server list based on conditions and priorities.

## Supported Platforms

<a href="https://www.spigotmc.org" target="_blank">
  <img src="https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@2/assets/minimal/supported/spigot_vector.svg" height="64" alt="platform-spigot" title="Tested on Spigot">
</a>
<a href="https://papermc.io" target="_blank">
  <img src="https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@2/assets/minimal/supported/paper_vector.svg" height="64" alt="platform-paper" title="Tested on Paper">
</a>
<a href="https://www.spigotmc.org" target="_blank">
  <img src="https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@2/assets/minimal/supported/bungeecord_vector.svg" height="64" alt="platform-bungeecord" title="Tested on BungeeCord">
</a>
<a href="https://www.papermc.io" target="_blank">
  <img src="https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@2/assets/minimal/supported/waterfall_vector.svg" height="64" alt="platform-waterfall" title="Tested on Waterfall">
</a>
<a href="https://velocitypowered.com" target="_blank">
  <img src="https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@2/assets/minimal/supported/velocity_vector.svg" height="64" alt="platform-velocity" title="Tested on Velocity">
</a>

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

<a href="https://modrinth.com/plugin/advancedserverlist/gallery" target="_blank">
  <img src="https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@2/assets/cozy/documentation/modrinth-gallery_vector.svg" height="64" alt="gallery" title="Check out the Gallery">
</a>

## Support
<a href="https://discord.gg/6dazXp6" target="_blank">
  <img src="https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@2/assets/minimal/social/discord-singular_vector.svg" height="64" alt="discord" title="Join my Discord Server">
</a>
<a href="https://app.revolt.chat/invite/74TpERXA" target="_blank">
  <img src="https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@2/assets/minimal/social/revolt-singular_vector.svg" height="64" alt="revolt" title="Join my Revolt Server">
</a>
<a href="https://blobfox.coffee/@andre_601" target="_blank">
  <img src="https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@2/assets/minimal/social/mastodon-singular_vector.svg" height="64" alt="mastodon" title="Chat with me on Mastodon">
</a>

<!-- Links -->
[profiles]: https://github.com/Andre601/AdvancedServerList/wiki/Profiles
[conditions]: https://github.com/Andre601/AdvancedServerList/wiki/Profiles#conditions
[placeholders]: https://github.com/Andre601/AdvancedServerList/wiki/Profiles#placeholders
[minimessage]: https://github.com/Andre601/AdvancedServerList/wiki/Profiles#minimessage

[placeholderapi]: https://www.spigotmc.org/resources/6245/

[modrinth]: https://modrinth.com/plugin/advancedserverlist

[protocollib]: https://www.spigotmc.org/resources/1997/
[placeholderapi]: https://www.spigotmc.org/resources/6245/

[bstats]: https://bstats.org
[bstats-bungee]: https://bstats.org/plugin/bungeecord/AdvancedServerList/15585
[bstats-spigot]: https://bstats.org/plugin/bukkit/AdvancedServerList/15584
[bstats-velocity]: https://bstats.org/plugin/velocity/AdvancedServerList/15587

[gallery]: https://modrinth.com/mod/advancedserverlist/gallery
