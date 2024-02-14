---
icon: octicons/pencil-24
---

# Formatting

All text options, with exception of [`Conditions`](../index.md#conditions), allow the usage of formatting options using [MiniMessage].

/// info | Online Tool
The devs of the Adventure library provide a handy online tool to create the right formatting codes to display the text properly.  
You can find the tool at https://webui.advntr.dev/
///

## Before starting

Please make sure to surround your text with either single quotes (`'`) or double quotes (`"`).  
This avoids possible issues where the YAML parser would treat lines starting with specific characters as special options (i.e. `<` would be treated as a scolar value).

```yaml title="Wrong formatting"
motd:
  - <red>This will cause
  - <red>Errors
```

```yaml title="Right formatting"
motd:
  - '<green>This will be formatted'
  - '<green>properly.'
```

## Unsupported options

The following options are **not** supported, no matter what option they are used in:

- Hover Actions (Show text, Show Advancement, etc)
- Click Actions (Run command, Suggest command, etc.)
- Custom Fonts (May work if player already has the resource pack loaded)

## Colors

### Default colors

Default colors such as `<red>`, `<green>`, `<aqua>`, etc. may be used in all text options.

### Hex Colors

Hexadecimal colors may be used in the [`motd`](index.md#motd) option using the `<#RRGGBB>` format.

### Gradients

Gradients can be created by using `<gradient:<color1>:<color2>>`, replacing `<color1>` with a starting color name or hex color value and `<color2>` with an ending color name or hex color value.  
Only the [`motd`](index.md#motd) option may support hex color gradients and in all other options will it be downsampled.

## Formatting

Formatting options (`<bold>`, `<italic>`, etc.) are available for all text options.

[MiniMessage]: https://docs.adventure.kyori.net/minimessage/index.html
