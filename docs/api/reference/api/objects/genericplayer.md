---
template: api-doc.html

methods:
  - name: 'getName'
    description: |
      Returns the name of the player.<br>
      Note that on Spigot, Paper and other forks can the name differ from the one cached by AdvancedServerList, if the plugin was able to retrieve an OfflinePlayer instance from the server. On BungeeCord and Velocity will the returned String always be the name from the cache.<br>
      <br>
      Name may be whatever has been defined in AdvancedServerList's config.yml, should the player not be cached yet by the plugin.
    returns: 'String representing the player''s name'
    type:
      name: String
      type: object
  - name: 'getProtocol'
    description: |
      Returns the protocol ID the player is using.<br>
      The protocol ID is an integer used by Minecraft to determine what version a server or client is running.
    returns: 'Integer representing the protocol version of this player.'
    type:
      name: int
  - name: 'getUUID'
    description: |
      Returns the unique ID associated with this player.<br>
      <br>
      UUID may be whatever has been defined in AdvancedServerList's config.yml, should the player not be cached yet by AdvancedServerList.
    returns: 'UUID of the player'
    type:
      name: UUID
      type: object
---

# <api__interface></api__interface> GenericPlayer

A basic interface used to store generic player data to use in placeholder resolving.  
The different variants of AdvancedServerList implement this into their own class and may provide additional data not available through this interface.

The following values will always be present, no matter the platform this interface is used on:

- [Name of the Player](#getname())
- [Protocol version of the Player](#getprotocol())
- [UUID of the Player](#getuuid())