# <span class="api-type__primitive">enum</span> NullBool

Enum used to return a `Boolean` value with a default one should no value be set.

## Enum Constant Summary

| Enum Constant   | Description |
|-----------------|-------------|
| [`TRUE`](#true) | Boolean value `true` |
| [`FALSE`](#false) | Boolean value `false` |
| [`NOT_SET`](#not_set) | Boolean value `null` |

----

## Method Summary

| Modifier and Type                          | Method                                            |
|--------------------------------------------|---------------------------------------------------|
| [`static NullBool`](#resolve(boolean))     | [`resolve(Boolean)`](#resolve(boolean))           |
| [`public boolean`](#isnotset())            | [`isNotSet()`](#isnotset())                       |
| [`public boolean`](#getordefault(boolean)) | [`getOrDefault(boolean)`](#getordefault(boolean)) |

----

## Enum Constant Detail

### <span class="api-label api__static"></span> <span class="api-label api__final"></span> <span class="api-type__class">NullBool</span> `TRUE` { #true }

Boolean value `true`

### <span class="api-label api__static"></span> <span class="api-label api__final"></span> <span class="api-type__class">NullBool</span> `FALSE` { #false }

Boolean value `false`

### <span class="api-label api__static"></span> <span class="api-label api__final"></span> <span class="api-type__class">NullBool</span> `NOT_SET` { #not_set }

Boolean value `null`

----

## Method Detail

### <span class="api-label api__static"></span> <span class="api-type__class">NullBool</span> `resolve(Boolean)` { #resolve(boolean) }

Returns a NullBool instance based on the provided `Boolean` value.  
In the case of `null` being provided will [`NullBool.NOT_SET`](#not_set) be returned, otherwise will the corresponding NullBool inszance matching the Boolean value be returned.

<h4>Parameters:</h4>

- `bool` - The `Boolean` value to receive a NullBool instance for.

<h4>Returns:</h4>

NullBool instance based on the provided `Boolean` value.

### <span class="api-type__primitive">boolean</span> `isNotSet()` { #isnotset() }

Returns whether the NullBool instance is [`NullBool.NOT_SET`](#not_set).

<h4>Returns:</h4>

True if the instance is [`NullBool.NOT_SET`](#not_set), otherwise false.

### <span class="api-type__primitive">boolean</span> `getOrDefault(boolean)` { #getordefault(boolean) }

Gets the corresponding boolean value associated with the NullBool instance.  
In the case of NullBool [not being set](#isnotset()) will the provided default value be returned.

<h4>Parameters:</h4>

- `def` - The default boolean value to return should the NullBool instance be [`NullBool.NOT_SET`](#not_set).

<h4>Returns:</h4>

true or false depending on the NullBool instance and the provided default value.