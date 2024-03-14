---
template: api-doc.html

constructors:
  - name: 'PostServerListSetEvent'
    description: 'Constructor for creating a new PostServerListSetEvent instance.'
    parameters:
      - name: 'entry'
        description: 'The <a href="../../../../api/ch.andre601.advancedserverlist.api/profiles/profileentry/"><code>ProfileEntry</code></a> used during the PingEvent.'
        type: ProfileEntry
        attribute:
          - nullable

methods:
  - name: 'getEntry'
    description: |
      The <a href="../../../../api/ch.andre601.advancedserverlist.api/profiles/profileentry/"><code>ProfileEntry</code></a> that was used during the PingEvent handling.<br>
      <br>
      The returned value may be <code>null</code> should the PingEvent handling be cancelled at any point (i.e. due to an invalid protocol or because the <a href="../preserverlistsetevent/"><code>PreServerListSetEvent</code></a> has been cancelled).<br>
      The values within the ProfileEntry also do not represent what is actually used within the Server list itself, but only the values provided for the PingEvent handling.
    returns: 'Possibly-null ProfileEntry used for the PingEvent.'
    attributes:
      - nullable
    type:
      name: 'ProfileEntry'
      type: 'object'
      link: '../../../../api/ch.andre601.advancedserverlist.api/profiles/profileentry/'
---

# <api__class></api__class> PostServerListSetEvent

Called **after** the plugin has updated the Server List.

The provided [`ProfileEntry`](#getentry()) may be null due to the PingEvent handling being cancelled (i.e. due to a invalid protocol or due to the [`PreServerListSetEvent`](preserverlistsetevent.md) being cancelled).  
The entry also only represents the values used during the PingEvent, not the actual content that is being displayed.