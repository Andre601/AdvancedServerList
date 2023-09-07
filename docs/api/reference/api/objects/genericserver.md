---
template: api-doc.html

methods:
  - name: 'getPlayersOnline'
    description: 'Returns the number of players currently online on the server.'
    returns: 'Number of players online on the server.'
    type:
      name: int
  - name: 'getPlayersMax'
    description: 'Returns the number of total players that can join the server.'
    returns: 'Number of total players that can join the server.'
    type:
      name: int
  - name: 'getHost'
    description: 'Returns the IP/Domain that got pinged by the player.'
    returns: 'Possibly-null String containing the IP/Domain that got pinged by the player'
    attributes:
      - nullable
    type:
      name: 'String'
      type: 'object'
---

# <api__interface></api__interface> GenericServer

Simple class used to wrap around some generic server data such as online player count, number of total players that can join and the host (IP/Domain) that got pinged by the player.