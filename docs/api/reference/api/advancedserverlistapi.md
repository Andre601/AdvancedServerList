# <span class="api-type__primitive">class</span> AdvancedServerListAPI

Core class of the API for AdvancedServerList.  
Use [`get()`](#get) to retrieve the instance currently used.

## Method Summary

| Modifier and Type                                             | Method                                                                                        |
|---------------------------------------------------------------|-----------------------------------------------------------------------------------------------|
| [`static AdvancedServerlistAPI`](#get())                      | [`get()`](#get())                                                                             |
| [`void`](#addplaceholderprovider(placeholderprovider))        | [`addPlaceholderProvider(PlaceholderProvider)`](#addplaceholderprovider(placeholderprovider)) |
| [`PlaceholderProvider`](#retrieveplaceholderprovider(string)) | [`retrievePlaceholderProvider(String)`](#retrieveplaceholderprovider(string))                 |

----

## Method Detail

### <span class="api-label api__static"></span> <span class="api-type__class">AdvancedServerListAPI</span> `get()` { #get() }

Retrieves the instance used of this API.  
If no instance has been made so far will a new one be created.

<h4>Returns</h4>

Instance of this API.

### <span class="api-type__primitive">void</span> `addPlaceholderProvider(PlaceholderProvider)` { #addplaceholderprovider(placeholderprovider) }

Adds the provided [`PlaceholderProvider`](placeholderprovider.md) to the list, if it passes the following checks:

- Provided PlaceholderProvider is not null.
- The identifier is not null or empty.
- The identifier does not contain any spaces.
- A PlaceholderProvider with the same identifier doesn't exist already.

Not passing any of the above checks results in a [`InvalidPlaceholderProviderException`](./exceptions/invalidplaceholderproviderexception.md) being thrown.

<h4>Parameters:</h4>

- `placeholderProvider` - The [`PlaceholderProvider`](placeholderprovider.md) to add.

<h4>Throws</h4>

- [`InvalidPlaceholderProviderException`](./exceptions/invalidplaceholderproviderexception.md) - When the provided [`PlaceholderProvider` instance](placeholderprovider.md) has a null or empty identifier, the identifier contains spaces or another provider with the same identifier is already loaded.

### <a href="../placeholderprovider" class="api-type__class">PlaceholderProvider</a> `retrievePlaceholderProvider(String)` { #retrieveplaceholderprovider(string) }

Retrieves the [`PlaceholderProvider`](placeholderprovider.md) associated with the given identifier, or `null` should no such entry exist.

<h4>Parameters:</h4>

- `identifier` - The identifier to find a matching [`PlaceholderProvider`](placeholderprovider.md) for.

<h4>Returns:</h4>

Possibly-null [`PlaceholderProvider` instance](placeholderprovider.md)