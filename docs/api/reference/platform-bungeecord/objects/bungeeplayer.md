---
template: api-doc.html

inherits:
  'ch.andre601.advancedserverlist.api.objects.GenericPlayer':
    link: '../../../api/objects/genericplayer/'
    list:
      - 'getName()'
      - 'getProtocol()'
      - 'getUUID()'
---

# <api__interface></api__interface> BungeePlayer

[`GenericPlayer` instance](../../api/objects/genericplayer.md) for the BungeeCord proxy implementation of AdvancedServerList.

To get an instance of this class from a GenericPlayer instance, simply cast it to a BungeePlayer (Granted that the GenericPlayer instance actually is a BungeePlayer instance).