---
template: api-doc.html

constructors:
  - name: 'PreServerListSetEvent'
    parameters:
      - name: 'entry'
        type: ProfileEntry

inherits:
  'ch.andre601.advancedserverlist.api.events.GenericServerListEvent':
    link: '../../../api/events/genericserverlistevent/'
    list:
      - 'getEntry()'
      - 'setEntry(ProfileEntry)'
      - 'isCancelled()'
      - 'setCancelled(boolean)'
---

# <api__class></api__class> PreServerListSetEvent

Called **before** AdvancedServerList modifies the server list.  
The provided [`ProfileEntry`](#getentry()) will be the one used for the server list.