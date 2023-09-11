---
template: api-doc.html

methods:
  - name: 'getWorlds'
    description: 'Returns a <code>Map&lt;String, World&gt;</code> where the key is the name of the World and the value the world of the server.'
    returns: 'Possibly-empty Map containing pairs of Strings and Worlds.'
    type:
      name: 'Map<String, World>'
      type: 'object'

inherits:
  'ch.andre601.advancedserverlist.api.objects.GenericServer':
    link: '../../../../api/ch.andre601.advancedserverlist.api/objects/genericserver/'
    list:
      - 'getPlayersOnline()'
      - 'getPlayersMax()'
      - 'getHost()'
---

# <api__interface></api__interface> BukkitServer

Spigot/Paper specific instance of a [`GenericServer`](../../../api/ch.andre601.advancedserverlist.api/objects/genericserver.md).  
This interface includes a [Map of Worlds](#getworlds()) the server currently has.

This interface is useful for cases where you want to get the worlds of the Server itself, by simply casting your GenericServer instance to a BukkitServer one. Make sure to do proper instanceof checks first before attempting to cast.