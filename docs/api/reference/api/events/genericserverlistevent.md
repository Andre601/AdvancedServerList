---
template: api-doc.html

methods:
  - name: 'getEntry()'
    description: 'Gets the <a href="../../profiles/profileentry/"><code>ProfileEntry</code></a> currently set.'
    returns: The currently used ProfileEntry.
    type:
      name: ProfileEntry
      type: object
      link: '../../profiles/profileentry/'
  - name: 'setEntry(ProfileEntry)'
    description: |-
      Sets the new <a href="../../profiles/profileentry/"><code>ProfileEntry</code></a> to use.<br>
      This may not be <code>null</code>.
    parameters:
      - name: 'entry'
        description: 'The new <a href="../../profiles/profileentry/"><code>ProfileEntry</code></a> to use.'
        attributes:
          - notnull
    throws:
      - name: 'IllegalArgumentException'
        description: 'When the provided ProfileEntry is <code>null</code>'
  - name: 'isCancelled()'
    description: Returns whether this event has been cancelled or not.
    returns: Whether this event has been cancelled or not.
    type:
      name: boolean
  - name: 'setCancelled(boolean)'
    description: Sets the event's cancel state
    parameters:
      - name: 'cancelled'
        description: 'Boolean to set the event''s cancelled state.'
---

# <api__interface></api__interface> GenericServerListEvent

Interface used for the platform-specific PreServerListSetEvent instances.  
This allows the plugin to pull common info such as ProfileEntry or if the event has been cancelled by another plugin.