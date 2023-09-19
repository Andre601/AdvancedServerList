---
template: api-doc.html

constructors:
  - name: 'Builder'
  
methods:
  - name: 'setMotd'
    description: |
      Sets a new MOTD to use.<br>
      <br>
      Set to an empty list to not change the MOTD.<br>
      Only the first two entries of the list will be considered and any additional ones discarded.
    returns: 'This Builder after the motd has been set. Useful for chaining.'
    parameters:
      - name: 'motd'
        description: 'The MOTD to use.'
        type: 'List<String>'
        attributes:
          - notnull
    throws:
      - name: 'IllegalArgumentException'
        description: 'Thrown by the <code>CheckUtil</code> in case <code>null</code> has been provided as parameter.'
    type:
      name: 'Builder'
      type: 'object'
  - name: 'setPlayers'
    description: |
      Sets the players (lines) to use for the hover.<br>
      <br>
      Set to an empty list to not change the hover text.
    returns: 'This Builder after the players have been set. Useful for chaining.'
    parameters:
      - name: 'players'
        description: 'The lines to set for the hover.'
        type: 'List<String>'
        attributes:
          - notnull
    throws:
      - name: 'IllegalArgumentException'
        description: 'Thrown by the <code>CheckUtil</code> in case <code>null</code> has been provided as parameter.'
    type:
      name: 'Builder'
      type: 'object'
  - name: 'setPlayerCountText'
    description: |
      Sets the text to override the player count with.<br>
      <br>
      Set to an empty String or <code>null</code> to not alter the player count text.
    returns: 'This Builder after the player count text has been set. Useful for chaining.'
    parameters:
      - name: 'playerCountText'
        description: 'The text to show in the player count.'
        type: String
        attributes:
          - nullable
    type:
      name: 'Builder'
      type: 'object'
  - name: 'setFavicon'
    description: |
      Sets the value to use for the favicon.<br>
      The following values are supported:
      <ul>
        <li>URL to a valid PNG file.</li>
        <li>File name (with <code>.png</code> extension) matching a file saved in the favicons folder of AdvancedServerList.</li>
        <li><code>${player uuid}</code> to display the avatar of the player.</li>
      </ul>
    returns: 'This Builder after the favicon has been set. Useful for chaining.'
    parameters:
      - name: 'favicon'
        description: 'The favicon to set'
        type: String
        attributes:
          - nullable
    type:
      name: 'Builder'
      type: 'object'
  - name: 'setHidePlayersEnabled'
    description: |
      Sets whether the player count should be hidden or not.<br>
      <br>
      Set to <a href="../../objects/nullbool/#not_set"><code>NullBool.NOT_SET</code></a> to not set this.
    returns: 'This Builder after the NullBool has been set. Useful for chaining.'
    parameters:
      - name: 'hidePlayersEnabled'
        description: 'Whether the player count should be hidden or not.'
        type: NullBool
        attributes:
          - notnull
    throws:
      - name: 'IllegalArgumentException'
        description: 'Thrown by the <code>CheckUtil</code> in case <code>null</code> has been provided as parameter.'
    type:
      name: 'Builder'
      type: 'object'
  - name: 'setExtraPlayersEnabled'
    description: |
      Sets whether the extra players feature should be enabled or not.<br>
      <br>
      Set to <a href="../../objects/nullbool/#not_set"><code>NullBool.NOT_SET</code></a> to not set this.
    returns: 'This Builder after the NullBool has been set. Useful for chaining.'
    parameters:
      - name: 'extraPlayersEnabled'
        description: 'Whether the extra players feature should be enabled or not.'
        type: NullBool
        attributes:
          - notnull
    throws:
      - name: 'IllegalArgumentException'
        description: 'Thrown by the <code>CheckUtil</code> in case <code>null</code> has been provided as parameter.'
    type:
      name: 'Builder'
      type: 'object'
  - name: 'setMaxPlayersEnabled'
    description: |
      Sets whether the max players feature should be enabled.<br>
      <br>
      Set to <a href="../../objects/nullbool/#not_set"><code>NullBool.NOT_SET</code></a> to not set this.
    returns: 'This Builder after the NullBool has been set. Useful for chaining.'
    parameters:
      - name: 'maxPlayersEnabled'
        description: 'Whether the extra players feature should be enabled or not.'
        type: NullBool
        attributes:
          - notnull
    throws:
      - name: 'IllegalArgumentException'
        description: 'Thrown by the <code>CheckUtil</code> in case <code>null</code> has been provided as parameter.'
    type:
      name: 'Builder'
      type: 'object'
  - name: 'setExtraPlayerCount'
    description: |
      Sets the number of players to add to the online players to use as the new max players value.<br>
      This option has no effect when <a href="../#extraplayersenabled()"><code>extraPlayersEnabled</code></a> is set to <a href="../../objects/nullbool/#false"><code>NullBool.FALSE</code></a> or <a href="../../objects/nullbool/#not_set"><code>NullBool.NOT_SET</code></a>.<br>
      <br>
      Set this to <code>null</code> to not alter the max player count. Alternatively, set <a href="#setextraplayersenabled(nullbool)"><code>setExtraPlayersEnabled(NullBool)</code></a> to <a href="../../objects/nullbool/#false"><code>NullBool.FALSE</code></a>.
    returns: 'This Builder after the extra player count has been set. Useful for chaining.'
    parameters:
      - name: 'extraPlayersCount'
        description: 'The number of extra players to add.'
        type: Integer
        attributes:
          - nullable
    deprecated: 'Typo in the name. Use <a href="#setextraplayerscount(integer)">setExtraPlayersCount(Integer)</a> instead.'
    type:
      name: 'Builder'
      type: 'object'
  - name: 'setExtraPlayersCount'
    description: |
      Sets the number of players to add to the online players to use as the new max players value.<br>
      This option has no effect when <a href="../#extraplayersenabled()"><code>extraPlayersEnabled</code></a> is set to <a href="../../objects/nullbool/#false"><code>NullBool.FALSE</code></a> or <a href="../../objects/nullbool/#not_set"><code>NullBool.NOT_SET</code></a>.<br>
      <br>
      Set this to <code>null</code> to not alter the max player count. Alternatively, set <a href="#setextraplayersenabled(nullbool)"><code>setExtraPlayersEnabled(NullBool)</code></a> to <a href="../../objects/nullbool/#false"><code>NullBool.FALSE</code></a>.
    returns: 'This Builder after the extra player count has been set. Useful for chaining.'
    parameters:
      - name: 'extraPlayersCount'
        description: 'The number of extra players to add.'
        type: Integer
        attributes:
          - nullable
    type:
      name: 'Builder'
      type: 'object'
  - name: 'setMaxPlayersCount'
    description: |
      Sets the number of max players allowed to join this server.<br>
      This option has no effect when <a href="../#maxplayersenabled()"><code>maxPlayersEnabled</code></a> is set to <a href="../../objects/nullbool/#false"><code>NullBool.FALSE</code></a> or <a href="../../objects/nullbool/#not_set"><code>NullBool.NOT_SET</code></a>.<br>
      <br>
      Set this to <code>null</code> to not alter the max player count. Alternatively, set <a href="#setmaxplayersenabled(nullbool)"><code>setMaxPlayersEnabled(NullBool)</code></a> to <a href="../../objects/nullbool/#false"><code>NullBool.FALSE</code></a>.
    returns: 'This Builder after the extra player count has been set. Useful for chaining.'
    parameters:
      - name: 'maxPlayersCount'
        description: 'The number of max players to set.'
        type: Integer
        attributes:
          - nullable
    type:
      name: 'Builder'
      type: 'object'
  - name: 'build'
    description: 'Creates a new <a href="./.."><code>ProfileEntry</code> instance</a> with the values set in this Builder.'
    returns: 'New <a href="./.."><code>ProfileEntry</code> instance</a>.'
    type:
      name: 'ProfileEntry'
      type: 'object'
      link: './..'
---

# <api__class></api__class> Builder

Builder class to create a new [`ProfileEntry` instance](index.md).