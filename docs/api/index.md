---
icon: octicons/command-palette-24
---

# API

AdvancedServerList v2 introduced a new API that plugins can hook into to use.  
It provides a way for your plugin to provide its own placeholders that should be parsed by AdvancedServerList and also an event to modify the server list.

## Add dependency

Add the following to your `pom.xml`, `build.gradle` or `build.gradle.kts` depending on what you use:

=== ":simple-apachemaven: Maven"
    
    ```xml title="pom.xml"
    <repositories>
      <repository>
        <id>jitpack</id>
        <url>https://jitpack.io/</url>
      </repository>
    </repositories>
    
    <dependencies>
      <dependency>
        <groupId>ch.andre601.asl-api</groupId>
        <artifactId>api</artifactId>
        <version>{version}</version>
        <scope>provided</scope>
      </dependency>
      
      <!-- Optional platform dependencies -->
      <dependency>
        <groupId>ch.andre601.asl-api</groupId>
        <artifactId>platform-bukkit</artifactId>
        <version>{version}</version>
        <scope>provided</scope>
      </dependency>
      <dependency>
        <groupId>ch.andre601.asl-api</groupId>
        <artifactId>platform-bungeecord</artifactId>
        <version>{version}</version>
        <scope>provided</scope>
      </dependency>
      <dependency>
        <groupId>ch.andre601.asl-api</groupId>
        <artifactId>platform-velocity</artifactId>
        <version>{version}</version>
        <scope>provided</scope>
      </dependency>
    </dependencies>
    ```
=== ":simple-gradle: Gradle"
    
    ```groovy title="build.gradle"
    repositorories {
        maven { url = 'https://jitpack.io/' }
    }
    
    dependencies {
        compileOnly 'ch.andre601.asl-api:api:{version}'
        
        // Optional platform dependencies
        compileOnly 'ch.andre601.asl-api:platform-bukkit:{version}'
        compileOnly 'ch.andre601.asl-api:platform-bungeecord:{version}'
        compileOnly 'ch.andre601.asl-api:platform-velocity:{version}'
    }
    ```
=== ":simple-gradle: Gradle (KTS)"
    ```kotlin title="build.gradle.kts"
    repositories {
        maven("https://jitpack.io")
    }
    
    dependencies {
        compileOnly("ch.andre601.asl-api:api:{version}")
        
        // Optional platform dependencies
        compileOnly("ch.andre601.asl-api:platform-bukkit:{version}")
        compileOnly("ch.andre601.asl-api:platform-bungeecord:{version}")
        compileOnly("ch.andre601.asl-api:platform-velocity:{version}")
    }
    ```

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

!!! tip
    You can replace the Constructor with a String argument with a no-args constructor and set the identifier directly in the `super`:
    ```java
    public MyPlaceholders() {
        super("myplaceholders");
    }
    ```

The [`parsePlaceholder(String, GenericPlayer, GenericServer)`][parseplaceholder] method is used by AdvancedServerList to replace a matching placeholder with a value.  
What you return is completely up to you. Just keep in mind that returning `null` will be treated as an invalid placeholder by AdvancedServerList, resulting in the placeholder being returned as-is without any changes.

[placeholderprovider]: reference/api/ch.andre601.advancedserverlist.api/placeholderprovider.md
[parseplaceholder]: reference/api/ch.andre601.advancedserverlist.api/placeholderprovider.md#parseplaceholder(string,-genericplayer,-genericserver)

??? example "Example of final class"
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

=== ":simple-spigotmc: Spigot"
    === "Softdepend"
        ```yaml title="plugin.yml"
        name: "MyPlugin"
        author: "author"
        version: "1.0.0"
        
        main: "com.example.plugin.ExamplePlugin"
        
        softdepend:
          - AdvancedServerList
        ```
    === "Depend"
        ```yaml title="plugin.yml"
        name: "MyPlugin"
        author: "author"
        version: "1.0.0"
        
        main: "com.example.plugin.ExamplePlugin"
        
        depend:
          - AdvancedServerList
        ```
=== ":fontawesome-solid-paper-plane: Paper"
    === "Softdepend"
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
    === "Depend"
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
=== ":octicons-git-merge-24: BungeeCord"
    === "Softdepend"
        ```yaml title="bungee.yml"
        name: "MyPlugin"
        author: "author"
        version: "1.0.0"
        
        main: "com.example.plugin.ExamplePlugin"
        
        softDepends:
          - AdvancedServerList
        ```
    === "Depend"
        ```yaml title="bungee.yml"
        name: "MyPlugin"
        author: "author"
        version: "1.0.0"
        
        main: "com.example.plugin.ExamplePlugin"
        
        depends:
          - AdvancedServerList
        ```
=== ":octicons-git-merge-24: Velocity"
    === "Softdepend (File)"
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
    === "Depend (File)"
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
              "optional": false
            }
          ]
        }
        ```
    === "Softdepend (Annotation)"
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
    === "Depend (Annotation)"
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

### 4. You're done!

Your plugin should now hook into AdvancedServerList and register its own custom placeholders to use.

## Listening for events

<!-- admo:info This requires the platform specific dependencies -->

AdvancedServerList provides an Event that your plugin can listen for.  
The event is called `PreServerListSetEvent` and provides the following methods to use:

- [`getEntry()`][getentry] - Gets the currently set [`ProfileEntry`][profileentry].
- [`setEntry(ProfileEntry)`][setentry] - Sets a new [`ProfileEntry`][profileentry] to use.
- [`isCancelled()`][iscancelled] - Gets the current cancelled state of the event.
- [`setCancelled(boolean)`][setcancelled] - Sets the current cancelled state.

Setting the event's cancelled state to true will result in AdvancedServerList not altering the Server list.

Please check the server/proxy's documentation on how to listen for events with your plugin.

[getentry]: reference/api/ch.andre601.advancedserverlist.api/events/genericserverlistevent.md#getentry()
[setentry]: reference/api/ch.andre601.advancedserverlist.api/events/genericserverlistevent.md#setentry(profileentry)
[iscancelled]: reference/api/ch.andre601.advancedserverlist.api/events/genericserverlistevent.md#iscancelled()
[setcancelled]: reference/api/ch.andre601.advancedserverlist.api/events/genericserverlistevent.md#setcancelled(boolean)

[profileentry]: reference/api/ch.andre601.advancedserverlist.api/profiles/profileentry/index.md

### ProfileEntry

The ProfileEntry record is the core class used within the `PreServerListSetEvent`. It sets what values (MOTD, Favicon, Players, etc.) should be displayed.  
Note that the record is - by nature - immutable and that the [`Builder`][builder] class should be used to create a new ProfileEntry instance to use.

A convenience method exists to create a `Builder` instance from an existing `ProfileEntry` in case you only want to alter certain options while everything else unchanged.

This allows you to customize the Server list using your plugin. Just keep in mind that other plugins may also do the same, overriding your changes.

[builder]: reference/api/ch.andre601.advancedserverlist.api/profiles/profileentry/builder.md