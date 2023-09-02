---
template: api-doc.html

constructors:
  - name: 'PreServerListSetEvent'
    parameters:
      - name: 'entry'
        type: ProfileEntry

methods:
  - name: 'getHandlerList'
    returns: 'Static <code>HandlerList</code> instance.'
    attributes:
      - static
    type:
      name: 'HandlerList'
      type: 'object'

inherits:
  'ch.andre601.advancedserverlist.api.events.GenericServerListEvent':
    link: '../../../api/events/genericserverlistevent/'
    methods:
      - 'getEntry()'
      - 'setEntry(ProfileEntry)'
      - 'isCancelled()'
      - 'setCancelled(boolean)'
  'org.bukkit.event.Event':
    link: 'https://hub.spigotmc.org/javadocs/spigot/org/bukkit/event/Event.html'
    methods:
      - 'getHandlers()'
---

# <api__class></api__class> PreServerListSetEvent

Called **before** AdvancedServerList modifies the server list.  
The provided [`ProfileEntry`](#getentry()) will be the one used for the server list.