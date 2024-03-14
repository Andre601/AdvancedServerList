---
template: api-doc.html

constructors:
  - name: 'PlaceholderProvider'
    description: 'Constructor used to set the identifier for the class extending the PlaceholderProvider class itself.'
    parameters:
      - name: 'identifier'
        description: 'The identifier to use for the placeholder. Cannot be empty.'
        type: String
        attribute:
          - notnull

methods:
  - name: 'parsePlaceholder'
    description: |
      Method called by AdvancedServerList's StringReplacer class to replace any appearances of <code>${&lt;identifier&gt; &lt;placeholder&gt;}</code> with whatever value a matching PlaceholderProvider may return.<br>
      <br>
      Returning <code>null</code> will be treated as an invalid placeholder by the plugin, making it return the placeholder as-is without any changes.
    parameters:
      - name: 'placeholder'
        type: String
        description: 'The part of the Placeholder that comes after the identifier and before the closing curly bracket.'
      - name: 'player'
        type: GenericPlayer
        description: 'The <a href="./../objects/genericplayer/"><code>GenericPlayer</code> instance</a> used.'
      - name: 'server'
        type: GenericServer
        description: 'The <a href="./../objects/genericserver/"><code>GenericServer</code> instance</a> used.'
    returns: 'Possibly-parsed or possibly-null String to replace the placeholder with.'
    attribute:
      - nullable
      - abstract
    type:
      name: 'String'
      type: 'object'
  - name: 'getIdentifier'
    description: 'Returns the identifier used by this PlaceholderProvider instance.'
    returns: 'String representing the identifier of this PlaceholderProvider instance.'
    type:
      name: 'String'
      type: 'object'
---

# <api__abstract></api__abstract> <api__class></api__class> PlaceholderProvider

Abstract class that is used to provide your own Placeholder patterns for AdvancedServerList to parse.

In order for your class to be considered a valid PlaceholderProvider will you need to set the `identifier` to a non-null, non-empty value without having any spaces in it.  
Once set, use [`AdvancedServerListAPI#addPlaceholderProvider(PlaceholderProvider)`](advancedserverlistapi.md#addplaceholderprovider(placeholderprovider)) to register your class for AdvancedServerList to use.