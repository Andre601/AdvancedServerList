---
template: api-doc.html

methods:
  - name: 'getPlayer'
    description: 'Gives the OfflinePlayer embedded in this BukkitPlayer.'
    returns: 'OfflinePlayer instance'
    type:
      name: 'OfflinePlayer'
      type: 'object'
  - name: 'hasPlayedBefore'
    description: |
      Returns whether this player has played on the server before.<br>
      <br>
      The returned boolean may be inaccurate if AdvancedServerList was unable to obtain a valid OfflinePlayer instance.
    returns: 'Boolean indicating whether this player has played on the server before.'
    type:
      name: 'boolean'
  - name: 'isBanned'
    description: |
      Returns whether this player has been banned from the server.<br>
      <br>
      The returned boolean may be inaccurate if AdvancedServerList was unable to obtain a valid OfflinePlayer instance.
    returns: 'Boolean indicating whether this player has been banned from the server.'
    type:
      name: 'boolean'
  - name: 'isWhitelisted'
    description: |
      Returns whether this player is whitelisted on the server.<br>
      <br>
      The returned boolean may be inaccurate if AdvancedServerList was unable to obtain a valid OfflinePlayer instance.
    returns: 'Boolean indicating whether this player is whitelisted on the server.'
    type:
      name: 'boolean'

inherits:
  'ch.andre601.advancedserverlist.api.objects.GenericPlayer':
    link: '../../../api/objects/genericplayer/'
    methods:
      - 'getName()'
      - 'getProtocol()'
      - 'getUUID()'
---

# <api__interface></api__interface> BukkitPlayer

[`GenericPlayer` instance](../../api/objects/genericplayer.md) for the SpigotMC/Paper server implementation of AdvancedServerList.  
This interface includes a [`OfflinePlayer` instance](#getplayer()) obtained from the server the plugin runs on alongside some getters to get if the player [has played on the server before](#hasplayedbefore()), [is banned](#isbanned()) or [is whitelisted](#iswhitelisted()). These options actually require a proper OfflinePlayer instance to be present or will otherwise default to `false`.

This class is useful for cases where you want to use the OfflinePlayer. Simply cast the GenericPlayer instance to a SpigotPlayer (Granted that it actually is an instance of it to begin with).