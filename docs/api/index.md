# API

AdvancedServerList v2 introduced a new API that plugins can hook into to use.  
It provides a way for your plugin to provide its own placeholders that should be parsed by AdvancedServerList and also an event to modify the server list.

## Add dependency

Add the following to your `build.gradle` or `pom.xml` file to use the API:

=== ":simple-gradle: Gradle"
    
    Make sure to replace `{version}` with the latest version available in the [GitHub Repository][api-repo].
    
    ```groovy
    repositorories {
        maven { url = 'https://jitpack.io/' }
    }
    
    dependencies {
        implementation 'ch.andre601.asl-api:api:{version}'
        
        // Optional platform dependencies
        implementation 'ch.andre601.asl-api:platform-bungeecord:{version}'
        implementation 'ch.andre601.asl-api:platform-paper:{version}'
        implementation 'ch.andre601.asl-api:platform-spigot:{version}'
        implementation 'ch.andre601.asl-api:platform-velocity:{version}'
    }
    ```

=== ":simple-apachemaven: Maven"
    
    Make sure to replace `{version}` with the latest version available in the [GitHub Repository][api-repo].
    
    ```xml
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
      </dependency>
      
      <!-- Optional platform dependencies -->
      <dependency>
        <groupId>ch.andre601.asl-api</groupId>
        <artifactId>platform-bungeecord</artifactId>
        <version>{version}</version>
      </dependency>
      <dependency>
        <groupId>ch.andre601.asl-api</groupId>
        <artifactId>platform-spigot</artifactId>
        <version>{version}</version>
      </dependency>
      <dependency>
        <groupId>ch.andre601.asl-api</groupId>
        <artifactId>platform-velocity</artifactId>
        <version>{version}</version>
      </dependency>
    </dependencies>
    ```

[api-repo]: https://github.com/Andre601/asl-api

## Add own placeholders

To add your own placeholders will you need to do a few steps.

### 1) Get API instance

Use `AdvancedServerListAPI.get()` to retrieve an instance of the currently used AdvancedServerList API.  
It will be needed at a later point.

### 2) Create a Placeholder class

Make a new class that you want to use for the placeholders and let it extend the `PlaceholderProvider` class of AdvancedServerList.  
Your IDE should now tell you to implement/override some methods. Confirm this action and your class should look something alongside this:  
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

It's recommended to replace your generated constructor with a no-args one and set the identifier directly in the `super()`.  
For example:  
```java
public MyPlaceholders() {
    super("myplaceholders");
}
```

The next step now would be to handle the different placeholders. These are handled in the `parsePlaceholder` method you had to override.  
The `placeholder` String is whatever value was provided after the identifier in `${<identifier> <placeholder>}`. It can contain spaces.

Something to note is, that when returning `null` will AdvancedServerList understand it as an invalid placeholder and return it unchanged.

Here is a small example of the final class:  
```java
public class MyPlaceholders extends PlaceholderProvider {
    
    public MyPlaceholders() {
        super("myplaceholders");
    }
    
    @Override
    public String parsePlaceholder(String placeholder, GenericPlayer player, GenericServer server) {
        if(placeholder.equals("hello"))
            return "Hi!";
        
        return null;
    }
}
```

### 3) Register the placeholder class

All that is left to do now is to register your class as a new PlaceholderProvider instance. To do this, get the API instance you retrieved earlier and use `addPlaceholderProvider` with a new instance of your class.

Example:  
```java
AdvancedServerListAPI api = AdvancedServerListAPI.get();

api.addPlaceholderProvider(new MyPlaceholders());
```

## Events

!!! info "Note"
    The mentioned event is only accessible through one of the platform dependencies, as those are used in the platform's respective event handler system.

v2 of the API adds the `PreServerListSetEvent` to the different platform dependencies to use.  
It allows you to modify the ProfileEntry used for the player pinging the server/proxy or even cancelling the event altogether.

### ProfileEntry

The ProfileEntry class has been moved to the API in v2 allowing you to create your own versions using the available Builder class.

This class contains values from a Server List Profile file. The values can either be from a `profiles` list entry, one of the global options in the file, or a mix of both, depending on what options are present.  
You can create a new entry or copy an existing one (i.e. from the [`PreServerListSetEvent`](#events)) to modify and use in the server list by setting it in the aforementioned `PreServerListSetEvent`.
