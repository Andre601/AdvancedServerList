# Formatting

All text options with exception of [`Conditions`](../#conditions) allow the usage of formatting options using [MiniMessage].

!!! warning "IMPORTANT"
    Whenever your text starts with a colour or formatting code (`<aqua>`, `<blue>`, `<bold>`, `<italic>`, etc.) should it be surrounded with single or double quotes.  
    Not doing so will cause the YAML parses used - Configurate - to not see the value as a String, but something else, causing exceptions to appear.
    
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

!!! info "Online Tool"
    The devs of the Adventure library provide a handy online tool to create the right formatting codes to display the text properly.  
    You can find the tool at https://webui.advntr.dev/

## Unsupported options

The following options are **not** supported, no matter what option they are used in:

- Hover Actions (Show text, Show Advancement, etc)
- Click Actions (Run command, Suggest command, etc.)
- Custom Fonts (May work if player already has the resource pack loaded)

## Colors

Normal text colours such as `<aqua>` or `<red>` can be used in all text options.  

## HEX Colours

24-Bit HEX colours such as `<#ffffff>` can be used in the [`Motd`](../#motd) option on 1.16+ servers/proxies.

[MiniMessage]: https://docs.adventure.kyori.net/minimessage/index.html
