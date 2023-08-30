---
template: api-doc.html

constructors:
  - name: 'Builder()'
  
methods:
  - name: 'setMotd(List<String>)'
    description: |
      Sets a new MOTD to use.<br>
      <br>
      Set to an empty list to not change the MOTD.<br>
      Only the first two entries of the list will be considered and any additional ones discarded.
    returns: 'This Builder after the motd has been set. Useful for chaining.'
    parameters:
      - name: 'motd'
        description: 'The MOTD to use.'
        attributes:
          - notnull
    throws:
      - name: 'IllegalArgumentException'
        description: 'Thrown by the <code>CheckUtil</code> in case <code>null</code> has been provided as parameter.'
    type:
      name: 'Builder'
      type: 'object'
  - name: 'setPlayers(List<String>)'
    description: |
      Sets the players (lines) to use for the hover.<br>
      <br>
      Set to an empty list to not change the hover text.
    returns: 'This Builder after the players have been set. Useful for chaining.'
    parameters:
      - name: 'players'
        description: 'The lines to set for the hover.'
        attributes:
          - notnull
    throws:
      - name: 'IllegalArgumentException'
        description: 'Thrown by the <code>CheckUtil</code> in case <code>null</code> has been provided as parameter.'
    type:
      name: 'Builder'
      type: 'object'
  - name: 'setPlayerCountText(String)'
    description: |
      Sets the text to override the player count with.<br>
      <br>
      Set to an empty String or <code>null</code> to not alter the player count text.
    returns: 'This Builder after the player count text has been set. Useful for chaining.'
    parameters:
      - name: 'playerCountText'
        description: 'The text to show in the player count.'
        attributes:
          - nullable
    type:
      name: 'Builder'
      type: 'object'
  - name: 'setFavicon(String)'
    description: |
      Sets the value to use for the favicon.<br>
      The following values are supported:
      <ul>
        <li>URL to a valid PNG file.</li>
        <li>File name (with <code>.png</code> extension) matching a file saved in the favicons folder of AdvancedServerList.</li>
        <li><code>${player uuid}</code> to display the avatar of the player.
    returns: 'This Builder after the favicon has been set. Useful for chaining.'
    parameters:
      - name: 'favicon'
        description: 'The favicon to set'
        attributes:
          - nullable
    type:
      name: 'Builder'
      type: 'object'
  - name: 'setHidePlayersEnabled(NullBool)'
    description: |
      Sets whether the player count should be hidden or not.<br>
      <br>
      Set to <a href="../../objects/nullbool/#not_set"><code>NullBool.NOT_SET</code></a> to not set this.
    returns: 'This Builder after the NullBool has been set. Useful for chaining.'
    parameters:
      - name: 'hidePlayersEnabled'
        description: 'Whether the player count should be hidden or not.'
        attributes:
          - notnull
    throws:
      - name: 'IllegalArgumentException'
        description: 'Thrown by the <code>CheckUtil</code> in case <code>null</code> has been provided as parameter.'
    type:
      name: 'Builder'
      type: 'object'
  - name: 'setExtraPlayersEnabled(NullBool)'
    description: |
      Sets whether the extra players feature should be enabled or not.<br>
      <br>
      Set to <a href="../../objects/nullbool/#not_set"><code>NullBool.NOT_SET</code></a> to not set this.
    returns: 'This Builder after the NullBool has been set. Useful for chaining.'
    parameters:
      - name: 'extraPlayersEnabled'
        description: 'Whether the extra players feature should be enabled or not.'
        attributes:
          - notnull
    throws:
      - name: 'IllegalArgumentException'
        description: 'Thrown by the <code>CheckUtil</code> in case <code>null</code> has been provided as parameter.'
    type:
      name: 'Builder'
      type: 'object'
  - name: 'setExtraPlayers(Integer)'
    description: |
      Sets the number of players to add to the online players to use as the new max players value.<br>
      This option has no effect when <a href="../#extraplayersenabled()"><code>extraPlayersEnabled()</code></a> is set to <a href="../../objects/nullbool/#false"><code>NullBool.FALSE</code></a>.<br>
      <br>
      Set this to <code>null</code> to not alter the max player count. Alternatively, set <a href="#setextraplayersenabled(nullbool)"><code>setExtraPlayersEnabled(NullBool)</code></a> to <a href="../../objects/nullbool/#false"><code>NullBool.FALSE</code></a>.
    returns: 'This Builder after the extra player count has been set. Useful for chaining.'
    parameters:
      - name: 'extraPlayersCount'
        description: 'The number of extra players to use.'
        attributes:
          - nullable
    type:
      name: 'Builder'
      type: 'object'
  - name: 'build()'
    description: 'Creates a new <a href="./.."><code>ProfileEntry</code> instance</a> with the values set in this Builder.'
    returns: 'New <a href="./.."><code>ProfileEntry</code> instance</a>.'
    type:
      name: 'ProfileEntry'
      type: 'object'
      link: './..'
---

# <api__class></api__class> Builder

Builder class to create a new [`ProfileEntry` instance](index.md).