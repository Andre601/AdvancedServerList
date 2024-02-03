---
icon: octicons/arrow-right-24
---

# < v3.7.0 to v3.7.0

Version 3.7.0 of AdvancedServerList brought significant changes to its conditions system.  
Namely, the `conditions` option is now deprecated and planned for removal in a future release of the Plugin, and the new [`condition`](../profiles/index.md#condition) option should now be used instead.

## Migration

To migrate, all you have to do is take the entries of the old conditions option and convert them into a single String connected with `and` operators in between. See the [Example below](#example).

## Example

Here is a before and after example to help visualize the change:

/// tab | Before
```yaml
priority: 0

conditions:
  - '${player protocol} > 763'
  - '${player isWhitelisted} == true'

motd:
  - 'Welcome ${player name}!'
```
///

/// tab | After
```yaml
priority: 0

# The text can all be in one line. This is only for better readability.
condition: |
  ${player protocol} >= 763 and
  ${player isWhitelisted}

motd:
  - 'Welcome ${player name}!'
```
///

/// tab | Diff View
```diff
priority: 0

-conditions:
-  - '${player protocol} >= 763'
-  - '${player isWhitelisted} == true'
+condition: |
+  ${player protocol} >= 763 and
+  ${player isWhitelisted}
```
///