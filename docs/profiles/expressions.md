---
icon: octicons/file-code-24
---

Expressions are part of a Server List Profile's condition system and allows you to define when a profile should and should not be displayed.

This page covers what the Expression system of AdvancedServerList offers and can do.

## Content

- [Types](#types)
    - [Literals](#literals)
        - [Numbers](#numbers)
        - [Strings](#strings)
        - [Booleans](#booleans)
    - [Placeholders](#placeholders)
    - [Binary Operators](#binary-operators)
    - [Parenthesis](#parenthesis)
    - [Negation](#negation)
- [Credits](#credits)

## Types

An expression contains different types of components that are evaluated to return a boolean output of either `true` or `false`.  
Depending on the type(s) will the output be differently understood and handled.

### Literals

The following cases are considered literal values:

#### Numbers

Any number is considered an expression, meaning the below examples are considered valid:

/// example | Examples
```shell
0
1000
-7
47.2
```
///

#### Strings

A String in single or double quotes is a valid expression.

/// warning | Important
Specific keywords will be treated as [Binary Operators](#binary-operators) even if they are part of a word.  
As an example, `Sand` would cause the expression engine to see `S AND` due to parts of the word matching `and`.

To avoid this is it very important to always surround Strings with single or double quotes, as that tells the expression engine to treat as a String.
///

/// example | Examples
```shell
"Hello World!"
""
```
///

#### Booleans

Strings `true` and `false` are treated as boolean literals.

### Placeholders

Any [placeholder](placeholders.md) is a valid expression.

/// example | Examples
```shell
${player protocol}
${player name}

${server playersOnline}
${server host}
```
///

### Binary Operators

`<expression> <binary operator> <expression>` is a valid expression.

The following binary operators can be used for boolean evaluation:

| Operator      | True if                                                                                                     |
|---------------|-------------------------------------------------------------------------------------------------------------|
| `and` / `&&`  | Both expressions return true.                                                                               |
| `or` / `\|\|` | Either expression returns true.                                                                             |
| `==` / `=`    | Both expressions are equal (Case sensitive).                                                                |
| `=~` / `~`    | Both expressions are equal (Not Case sensitive).                                                            |
| `!=`          | Both expressions are not equal (Case sensitive).                                                            |
| `!~`          | Both expressions are not equal (Not case sensitive).                                                        |
| `\|-`         | Left expression starts with right expression.                                                               |
| `-\|`         | Left expression ends with right expression.                                                                 |
| `<_`          | Left expression contains right expression.                                                                  |
| `<`           | Left expression is less than the right expression. Strings use their text length as number.                 |
| `<=`          | Left expression is less than, or equal to, the right expression. Strings use their text length as number.   |
| `>`           | Left expression is larger than the right expression. Strings use their text length as number.               |
| `>=`          | Left expression is larger than, or equal to, the right expression. Strings use their text length as number. |

These additional binary operators can also be used to perform certain actions:

| Operator | Semantic                                                                    |
|----------|-----------------------------------------------------------------------------|
| `.`      | Concatenates (merges) two Strings                                           |
| `+`      | Adds two numbers. Strings use their text length as number.                  |
| `-`      | Subtracts one number from another. Strings use their text length as number. |
| `*`      | Multiplies two numbers. Strings use their text length as number.            |
| `/`      | Divides one number by another. Strings use their text length as number.     |

/// example | Examples
```shell
${player name} != "Anonymous"
763 > ${player protocol} > 758
${player hasPlayedBefore} and ${player isBanned} == "false"
```
///

### Parenthesis

`( <expression> )` is a valid expression.
Parenthesis can be used to prevent ambiguities.

### Negation

`!` can be used to negate boolean expressions.

```shell
!${player isWhitelisted}
```

## Credits

This page, including some examples used, was adobted from [:octicons-book-24: CodeCrafter47/BungeeTabListPlus/wiki](https://github.com/CodeCrafter47/BungeeTabListPlus/wiki/Expressions){ target="_blank" rel="nofollow" }.