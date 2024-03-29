---
icon: octicons/command-palette-24
---

# API

AdvancedServerList offers an API that other plugins can use to add their own placeholder to the plugin, listen for specific events or even modify Server List Profiles on the fly.

## Add dependency

Add the following to your `pom.xml`, `build.gradle` or `build.gradle.kts` depending on what you use:

/// tab | :simple-apachemaven: Maven 
```xml title="pom.xml"
<repositories>
  <repository>
    <id>codeberg</id>
    <url>https://codeberg.org/api/packages/Andre601/maven/</url>
  </repository>
</repositories>

<dependencies>
  <dependency>
    <groupId>ch.andre601.asl-api</groupId>
    <artifactId>api</artifactId>
    <version>{apiVersion}</version>
    <scope>provided</scope>
  </dependency>
  
  <!-- Optional platform dependencies -->
  <dependency>
    <groupId>ch.andre601.asl-api</groupId>
    <artifactId>platform-bukkit</artifactId>
    <version>{apiVersion}</version>
    <scope>provided</scope>
  </dependency>
  <dependency>
    <groupId>ch.andre601.asl-api</groupId>
    <artifactId>platform-bungeecord</artifactId>
    <version>{apiVersion}</version>
    <scope>provided</scope>
  </dependency>
  <dependency>
    <groupId>ch.andre601.asl-api</groupId>
    <artifactId>platform-velocity</artifactId>
    <version>{apiVersion}</version>
    <scope>provided</scope>
  </dependency>
</dependencies>
```
///

/// tab | :simple-gradle: Gradle
```groovy title="build.gradle"
repositorories {
    maven { url = 'https://codeberg.org/api/packages/Andre601/maven/' }
}

dependencies {
    compileOnly 'ch.andre601.asl-api:api:{apiVersion}'
    
    // Optional platform dependencies
    compileOnly 'ch.andre601.asl-api:platform-bukkit:{apiVersion}'
    compileOnly 'ch.andre601.asl-api:platform-bungeecord:{apiVersion}'
    compileOnly 'ch.andre601.asl-api:platform-velocity:{apiVersion}'
}
```
///

/// tab | :simple-gradle: Gradle (KTS)
```kotlin title="build.gradle.kts"
repositories {
    maven("https://codeberg.org/api/packages/Andre601/maven/")
}

dependencies {
    compileOnly("ch.andre601.asl-api:api:{apiVersion}")
    
    // Optional platform dependencies
    compileOnly("ch.andre601.asl-api:platform-bukkit:{apiVersion}")
    compileOnly("ch.andre601.asl-api:platform-bungeecord:{apiVersion}")
    compileOnly("ch.andre601.asl-api:platform-velocity:{apiVersion}")
}
```
///

## Adding your own Placeholders

<!-- admo:info This requires the main api module -->

The API allows you to add your own placeholders which would be available through the `${<identifier> <values>}` placeholder Pattern in a Server List profile.

To add your own placeholders, follow these steps:

### 1. Create a Placeholder class

You should first create a new class and make it extend the abstract [`PlaceholderProvider`][placeholderprovider]. This class includes a constructor and a method you need to add, so do that:

```java
public class MyPlaceholders extends PlaceholderProvider {
    
    public MyPlaceholders(String identifier) {
        super(identifier);
    }
    
    @Override
    public String parsePlaceholder(String placeholder, GenericPlayer player, GenericServer server) {
        return null;
    }
}
```

/// tip
You can replace the Constructor with a String argument with a no-args constructor and set the identifier directly in the `super`:
```java
public MyPlaceholders() {
    super("myplaceholders");
}
```
///

The [`parsePlaceholder(String, GenericPlayer, GenericServer)`][parseplaceholder] method is used by AdvancedServerList to replace a matching placeholder with a value.  
What you return is completely up to you. Just keep in mind that returning `null` will be treated as an invalid placeholder by AdvancedServerList, resulting in the placeholder being returned as-is without any changes.

[placeholderprovider]: reference/api/ch.andre601.advancedserverlist.api/placeholderprovider.md
[parseplaceholder]: reference/api/ch.andre601.advancedserverlist.api/placeholderprovider.md#parseplaceholder(string,-genericplayer,-genericserver)

/// details | Example of final class
    type: example

```java
public class MyPlaceholders extends PlaceholderProvider {
    
    public MyPlaceholders() {
        super("myplaceholders");
    }
    
    @Override
    public String parsePlaceholder(String placeholder, GenericPlayer player, GenericServer server) {
        if(placeholder.equalsIgnoreCase("hello"))
            return "Hello " + player.getName();
        
        return null;
    }
}
```
///

### 2. Register the Placeholders

Next step should be to register the placeholder. To achieve this, first obtain an instance of the [`AdvancedServerListAPI`][advancedserverlistapi] by using the static [`get()`][get] method. After that call [`addPlaceholderProvider(PlaceholderProvider placeholderProvider)`][addplaceholderprovider].  
Your code may look similar to this:

```java title="Registering PlaceholderProvider class"
public class MyPlugin extends JavaPlugin {
    
    @Override
    public void onEnable() {
        AdvancedServerListAPI api = AdvancedServerListAPI.get();
        
        api.addPlaceholderProvider(new MyPlaceholders());
    }
}
```

[advancedserverlistapi]: reference/api/ch.andre601.advancedserverlist.api/advancedserverlistapi.md
[get]: reference/api/ch.andre601.advancedserverlist.api/advancedserverlistapi.md#get()
[addplaceholderprovider]: reference/api/ch.andre601.advancedserverlist.api/advancedserverlistapi.md#addplaceholderprovider(placeholderprovider)

This should register your PlaceholderExpansion as long as it is valid, meaning that the identifier...

- ...is not `null`
- ...is not containing spaces
- ...is not using a name already registered in the API

### 3. Declare AdvancedServerList as (soft)depend

The final thing you should make sure is to define AdvancedServerList as a depend or soft-depend for your plugin, to make sure it loads after AdvancedServerList.

Below are example setups for Spigot, Paper, BungeeCord and Velocity:

/// tab | :simple-spigotmc: Spigot
//// tab | Softdepend
```yaml title="plugin.yml"
name: "MyPlugin"
author: "author"
version: "1.0.0"

main: "com.example.plugin.ExamplePlugin"

softdepend:
  - AdvancedServerList
```
////

//// tab | Depend
```yaml title="plugin.yml"
name: "MyPlugin"
author: "author"
version: "1.0.0"

main: "com.example.plugin.ExamplePlugin"

depend:
  - AdvancedServerList
```
////
///

/// tab | :fontawesome-solid-paper-plane: Paper
//// tab | Softdepend
```yaml title="paper-plugin.yml"
name: "MyPlugin"
author: "author"
version: "1.0.0"

main: "com.example.plugin.ExamplePlugin"

dependencies:
  server:
    AdvancedServerList:
      load: BEFORE
      required: false # Default, not required
```
////

//// tab | Depend
```yaml title="paper-plugin.yml"
name: "MyPlugin"
author: "author"
version: "1.0.0"

main: "com.example.plugin.ExamplePlugin"

dependencies:
  server:
    AdvancedServerList:
      load: BEFORE
      required: true
```
////
///

/// tab | :octicons-git-merge-24: BungeeCord
//// tab | Softdepend
```yaml title="bungee.yml"
name: "MyPlugin"
author: "author"
version: "1.0.0"

main: "com.example.plugin.ExamplePlugin"

softDepends:
  - AdvancedServerList
```
////

//// tab | Depend
```yaml title="bungee.yml"
name: "MyPlugin"
author: "author"
version: "1.0.0"

main: "com.example.plugin.ExamplePlugin"

depends:
  - AdvancedServerList
```
////
///

/// tab | :octicons-git-merge-24: Velocity
//// tab | Softdepend (File)
```json title="velocity-plugin.json"
{
  "id": "myplugin",
  "name": "MyPlugin",
  "version": "1.0.0",
  "authors": [
    "author"
  ],
  "main": "com.example.plugin.ExamplePlugin",
  "dependencies": [
    {
      "id": "advancedserverlist",
      "optional": true
    }
  ]
}
```
////

//// tab | Depend (File)
```json title="velocity-plugin.json"
{
  "id": "myplugin",
  "name": "MyPlugin",
  "version": "1.0.0",
  "authors": [
    "author"
  ],
  "main": "com.example.plugin.ExamplePlugin",
  "dependencies": [
    {
      "id": "advancedserverlist",
      "optional": false // Default, not required
    }
  ]
}
```
////

//// tab | Softdepend (Annotation)
```java title="MyPlugin.java"
@Plugin(
  id = "myplugin",
  name = "MyPlugin",
  version = "1.0.0",
  authors = {"author"},
  dependencies = {
    @Dependency(
      id = "advancedserverlist",
      optional = true
    )
  }
)
public class MyPlugin {
  
  // ...
  
}
```
////

//// tab | Depend (Annotation)
```java title="MyPlugin.java"
@Plugin(
  id = "myplugin",
  name = "MyPlugin",
  version = "1.0.0",
  authors = {"author"},
  dependencies = {
    @Dependency(
      id = "advancedserverlist",
      optional = false // Default, not required
    )
  }
)
public class MyPlugin {
  
  // ...
  
}
```
////
///

### 4. You're done!

Your plugin should now hook into AdvancedServerList and register its own custom placeholders to use.

## Listening for events

<!-- admo:info These events require the platform-specific APIs -->

AdvancedServerList provides Events that your plugin can listen for specific situations.

### PreServerListSetEvent

This event is called **before** AdvancedServerList starts handling the PingEvent of the Server/Proxy.  
It allows you to get and alter the [ProfileEntry][profileentry] used in the PingEvent, or even outright cancel the event itself.

The event offers the following methods:

- [`getEntry()`][getentry] - Gets the currently set [`ProfileEntry`][profileentry].
- [`setEntry(ProfileEntry)`][setentry] - Sets a new [`ProfileEntry`][profileentry] to use.
- [`isCancelled()`][iscancelled] - Gets the current cancelled state of the event.
- [`setCancelled(boolean)`][setcancelled] - Sets the current cancelled state.

Please check the server/proxy's documentation on how to listen for events with your plugin.

[getentry]: reference/api/ch.andre601.advancedserverlist.api/events/genericserverlistevent.md#getentry()
[setentry]: reference/api/ch.andre601.advancedserverlist.api/events/genericserverlistevent.md#setentry(profileentry)
[iscancelled]: reference/api/ch.andre601.advancedserverlist.api/events/genericserverlistevent.md#iscancelled()
[setcancelled]: reference/api/ch.andre601.advancedserverlist.api/events/genericserverlistevent.md#setcancelled(boolean)

### PostServerListSetEvent

This event is called **after** AdvancedServerList has completed its handling of the Server's/Proxy's PingEvent.

It provides a method to retrieve the [ProfileEntry][profileentry] used during the PingEvent.  
Do note that this ProfileEntry is **not** a representation of what is actually displayed in the server list, but rather what has been used by AdvancedServerList. It may also be `null` should the event handling get cancelled for any reason.

[profileentry]: reference/api/ch.andre601.advancedserverlist.api/profiles/profileentry/index.md

### ProfileEntry

The ProfileEntry record is the core class used within the `PreServerListSetEvent`. It sets what values (MOTD, Favicon, Players, etc.) should be displayed.  
Note that the record is - by nature - immutable and that the [`Builder`][builder] class should be used to create a new ProfileEntry instance to use.

A convenience method ([`builder()`][builder()]) exists to create a `Builder` instance from an existing `ProfileEntry` in case you only want to alter certain options while everything else should remain unchanged.

This allows you to customize the Server list using your plugin. Just keep in mind that other plugins may also do the same, overriding your changes.

[builder]: reference/api/ch.andre601.advancedserverlist.api/profiles/profileentry/builder.md
[builder()]: reference/api/ch.andre601.advancedserverlist.api/profiles/profileentry/#builder()