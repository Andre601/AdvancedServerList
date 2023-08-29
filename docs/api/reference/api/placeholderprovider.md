# <span class="api-label api__abstract"></span> <span class="api-type__primitive">class</span> PlaceholderProvider

Abstract class that is used to provide your own Placeholder patterns for AdvancedServerList to parse.

In order for your class to be considered a valid PlaceholderProvider will you need to set the `identifier` to a non-null, non-empty value without having any spaces in it.  
Once set, use [`AdvancedServerListAPI#addPlaceholderProvider(PlaceholderProvider)`](advancedserverlistapi.md#addplaceholderprovider(placeholderprovider)) to register your class for AdvancedServerList to use.

## Constructor Summary

| Constructor                                                   | Description                                                                                   |
|---------------------------------------------------------------|-----------------------------------------------------------------------------------------------|
| [`PlaceholderProvider(String)`](#placeholderprovider(string)) | Constructor used to set the identifier for the class extending the PlaceholderProvider class. |

## Method Summary

| Modifier and Type                                                          | Method                                                                                                            |
|----------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------------------------|
| [`abstract String`](#parseplaceholder(string-genericplayer-genericserver)) | [`parsePlaceholder(String, GenericPlayer, GenericServer)`](#parseplaceholder(string-genericplayer-genericserver)) |
| [`String`](#getidentifier())                                               | [`getIdentifier()`](#getidentifier())                                                                             |

----

## Constructor Detail

### `PlaceholderProvider(String)` { #placeholderprovider(string) }

Constructor used to set the identifier for the class extending the PlaceholderProvider class.

**Example:**
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

<h4>Parameters:</h4>

- `identifier` -  The identifier to use for the placeholder. Shouldn't be null nor empty.

----

## Method Detail

### <span class="api-label api__abstract"></span> <span class="api-type__class">String</span> `parsePlaceholder(String placeholder, GenericPlayer player, GenericServer server)` { #parsePlaceholder(string-genericplayer-genericserver) }

Method called by AdvancedServerList's StringReplacer class to replace any appearances of `${<identifier> <placeholder>}` with whatever value a PlacehholderProvider may return.

Returning `null` will be treated as an invalid placeholder, returning the full placeholder as-is without any changes made.

<h4>Parameters:</h4>

- `placeholder` - The part of the placeholder after the identifier (`${<identifier> <placeholder>}`)
- `player` - The [`GenericPlayer` instance](./objects/genericplayer.md) used.
- `server` - The [`GenericServer` instance](./objects/genericserver.md) used.

<h4>Returns:</h4>

Parsed String based on the PlaceholderProvider or `null` for invalid placeholders.

### <span class="api-type__class">String</span> `getIdentifier()` { #getIdentifier() }

Returns the identifier used by this instance

<h4>Returns:</h4>

String containing the identifier used by this instance.