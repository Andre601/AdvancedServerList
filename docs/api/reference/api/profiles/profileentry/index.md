---
template: api-doc.html

constructors:
  - name: 'ProfileEntry'
    description: |
      Creates a new instance of a ProfileEntry with the given values.<br>
      It's recommended to use the <a href="./builder/"><code>Builder</code> class</a> for a more convenient configuration of the Settings.
    parameters:
      - name: 'motd'
        description: 'The MOTD to use.'
        type: 'List<String>'
        attributes:
          - notnull
      - name: 'players'
        description: 'The players (Lines) to show in the hover.'
        type: 'List<String>'
        attributes:
          - notnull
      - name: 'playerCountText'
        description: 'The text to display instead of the player count.'
        type: String
        attributes:
          - nullable
      - name: 'favicon'
        description: 'The favicon to use.'
        type: String
        attributes:
          - nullable
      - name: 'hidePlayersEnabled'
        description: 'Whether player count should be hidden.'
        type: NullBool
        attributes:
          - notnull
      - name: 'extraPlayersEnabled'
        description: 'Whether the extra players option should be enabled.'
        type: NullBool
        attributes:
          - notnull
      - name: 'extraPlayersCount'
        description: 'The number to add to the online players for the max players number.'
        type: Integer
        attributes:
          - nullable
    seealso:
      - name: 'ProfileEntry.Builder'
        link: './builder/'

classes:
  - name: 'Builder'
    description: 'Builder class to create a new ProfileEntry instance.'
    type: 'static class'
    link: 'builder/'

methods:
  - name: 'empty'
    description: |
      Creates an "empty" PlayerEntry with the following values set:<br>
      <ul>
        <li><a href="#motd()"><code>motd</code></a>: Empty List</li>
        <li><a href="#players()"><code>players</code></a>: Empty List</li>
        <li><a href="#playercounttext()"><code>playerCountText</code></a>: Empty String</li>
        <li><a href="#favicon()"><code>favicon</code></a>: Empty String</li>
        <li><a href="#hideplayersenabled()"><code>hidePlayersEnabled</code></a>: <a href="../../objects/nullbool/"><code>NullBool.NOT_SET</code></a></li>
        <li><a href="#extraplayersenabled()"><code>extraPlayersEnabled</code></a>: <a href="../../objects/nullbool/"><code>NullBool.NOT_SET</code></a></li>
        <li><a href="#extraplayerscount()"><code>extraPlayersCount</code></a>: <code>null</code></li>
      </ul>
    returns: 'New ProfileEntry instance with empty/null values defined'
    attributes:
      - static
    type:
      name: 'ProfileEntry'
      type: 'object'
  - name: 'copy'
    description: |
      Creates a copy of this ProfileEntry instance.<br>
      <br>
      This is simply a convenience method to not have to call <a href="#builder()"><code>builder()</code></a><code>.</code><a href="./builder/#build()"><code>build()</code></a>.<br>
      If you want to modify the ProfileEntry <a href="#builder()"><code>builder()</code></a> is preferred.
    returns: 'A copy of this ProfileEntry instance.'
    seealso:
      - name: 'builder()'
        link: '#builder()'
    type:
      name: 'ProfileEntry'
      type: 'object'
  - name: 'builder'
    description: |
      Creates a <a href="./builder/"><code>Builder</code> instance</a> with the values from this ProfileEntry set.<br>
      Use this method if you would like to modify the ProfileEntry.
    returns: 'A new <a href="./builder/"><code>Builder</code> instance</a> with the values of this ProfileEntry set.'
    type:
      name: 'Builder'
      type: 'object'
      link: './builder/'
  - name: 'motd'
    description: 'Gets the currently set MOTD of this ProfileEntry.'
    returns: 'The current MOTD used by this ProfileEntry.'
    type:
      name: 'List<String>'
      type: 'object'
  - name: 'players'
    description: 'Gets the currently set players of this ProfileEntry.'
    returns: 'The current list of players used by this ProfileEntry.'
    type:
      name: 'List<String>'
      type: 'object'
  - name: 'playerCountText()'
    description: 'Gets the currently set player count text of this ProfileEntry.'
    returns: 'The current player count text used by this ProfileEntry.'
    type:
      name: 'String'
      type: 'object'
  - name: 'favicon'
    description: |
      Gets the currently set favicon of this ProfileEntry.<br>
      Note that the favicon usually is and supports one of the following options:
      <ul>
        <li>URL to a valid PNG file</li>
        <li>File name (with <code>.png</code> extension) matching a file saved in the favicons folder of AdvancedServerList</li>
        <li><code>${player uuid}</code> to display the avatar of the player</li>
      </ul>
    returns: 'The current favicon used by this ProfileEntry.'
    type:
      name: 'String'
      type: 'object'
  - name: 'hidePlayersEnabled'
    description: |
      Whether the player count should be hidden or not in this ProfileEntry.<br>
      To get the actual boolean value, append <a href="../../objects/nullbool/#getordefault()"><code>getOrDefault(boolean)</code></a>.
    returns: 'Whether the player count should be hidden or not in this ProfileEntry.'
    type:
      name: 'NullBool'
      type: 'object'
      link: '../../objects/nullbool/'
  - name: 'extraPlayersEnabled'
    description: |
      Whether the extra players feature should be used or not in this ProfileEntry.<br>
      To get the actual boolean value, append <a href="../../objects/nullbool/#getordefault()"><code>getOrDefault(boolean)</code></a>.
    returns: 'Whether the extra players feature should be used or not in this ProfileEntry.'
    type:
      name: 'NullBool'
      type: 'object'
      link: '../../objects/nullbool/'
  - name: 'extraPlayersCount'
    description: 'Gets the currently set number of extra players to use by this ProfileEntry. May be <code>null</code>.'
    returns: 'Possibly-null integer number of extra players used by this ProfileEntry.'
    attributes:
      - nullable
    type:
      name: 'Integer'
      type: 'object'
  - name: 'isInvalid'
    description: |
      Whether this ProfileEntry is considered invalid or not.<br>
      The ProfileEntry is considered invalid if all the following is true:
      <ul>
        <li><a href="#motd()"><code>motd()</code></a> is empty.</li>
        <li><a href="#players()"><code>players()</code></a> is empty.</li>
        <li><a href="#playercounttext()"><code>playerCountText()</code></a> is null/empty <b>and</b> <a href="#hideplayersenabled()"><code>hidePlayersEnabled()</code></a> is <code>false</code>.</li>
        <li><a href="#favicon()"><code>favicon()</code></a> is null/empty.</li>
      </ul>
      As long as one of the above is <b>not</b> true is this ProfileEntry considered valid and <code>false</code> will be returned.
    returns: 'Whether this ProfileEntry is invalid or not.'
    type:
      name: 'boolean'
      type: 'primitive'
---

# <api__record></api__record> ProfileEntry

This record represents the content found in a server list profile YAML file.  
The content may come from either the "profiles" list, the options in the file itself (global options) or a mix of both.

This class is immutable. Use [`builder()`](#builder()) to get a [`Builder` instance](builder.md) with the values of this class added.