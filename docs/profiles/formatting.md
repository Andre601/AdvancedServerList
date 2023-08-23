# Formatting

All text options, with exception of [`Conditions`](../#conditions), allow the usage of formatting options using [MiniMessage].

!!! info "Online Tool"
    The devs of the Adventure library provide a handy online tool to create the right formatting codes to display the text properly.  
    You can find the tool at https://webui.advntr.dev/

## Before starting

Please make sure to surround your text with either single quotes (`'`) or double quotes (`"`) should they start with a color or formatting code.  
Not surrounding these lines with (double) quotes will make the YAML parser Configurate treat them as something other than a String.

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

Color options (`<aqua>`, `<red>`, `<gray>`, etc.) are available for all text options.  
24-Bit HEX colors (`<#ff0000>`, `<#123abc>`, etc.) are only available for the [`Motd` option](../#motd) for a 1.16+ Server/Proxy.

## Formatting

Formatting options (`<bold>`, `<italic>`, etc.) are available for all text options.

[MiniMessage]: https://docs.adventure.kyori.net/minimessage/index.html
