# API Reference Docs Structure

The Reference docs follow a specific mixture of manually created and auto-generated page content.

A page may look something like this:
```markdown
---
template: api-doc.html

constructors:
  - name: 'Constructor'
    description: 'Creates a new instance of this class.'

methods:
  - name: 'getString'
    description: 'Gets a String.'
    type:
      name: String
      type: object
---

# <api__class></api__class> Constructor

An example class to show how the page structure may look like.
```

## custom HTML tags

There is a collection of custom HTML tags that are used to render specific text in certain styles.  
Below is a list of all custom tags. Note that these tags only render on the actual site and not within the repository.

- `<api__class></api__class>`: <api__class></api__class>
- `<api__interface></api__interface>`: <api__interface></api__interface>
- `<api__enum></api__enum>`: <api__enum></api__enum>
- `<api__record></api__record>`: <api__record></api__record>
- `<api__abstract></api__abstract>`: <api__abstract></api__abstract>
- `<api__static></api__static>`: <api__static></api__static>
- `<api__final></api__final>`: <api__final></api__final>
- `<api__notnull></api__notnull>`: <api__notnull></api__notnull>
- `<api__nullable></api__nullable>`: <api__nullable></api__nullable>
- `<api__deprecated></api__deprecated>`: <api__deprecated></api__deprecated>

In addition are there two classes used within `span` tags to colour the text, to indicate their type:

- `<span class="api-type__object">Object</span>`: <span class="api-type__object">Object</span>
- `<span class="api-type__primitive">Object</span>`: <span class="api-type__primitive">Primitive</span>

## Auto-generated content

The page template used checks for specific YAML frontmatter in the page to include other templates.  
These templates pull the data from the YAML frontmatter to generate the page content which is then appendet to the already existing page content.

There are 5 frontmatter options, each following a specific structure:

- [`Constructors`](#constructors)
- [`Nested Classes`](#nested-classes)
- [`Enum Constants`](#enum constants)
- [`Methods`](#methods)
- [`Inherits`](#inherits)

### Constructors

```yaml
constructors:
  - name: '<string>'
    description: '<string>'
    parameters:
      - name: '<string>'
        description: '<string>'
        type: '<string>'
        attributes:
          - '<string>'
    deprecated: '<string>'
    throws:
      - name: '<string>'
        description: '<string>'
    seealso:
      - name: '<string>'
        link: '<string>'
```

**Options:**

- `name` - The name to display for the Constructor summary and detail. This should include any parameters the Constructor has, but without any parameter name.
- `description` - The description to display for the Constructor.
- `parameters` - List of parameters to display.
    - `name` - Name of the parameter to display.
    - `description` - Description of the parameter.
    - `type` - The type this parameter is.
    - `attributes` - List of attributes (i.e. nullability) to display. The provided values will be turned into `<api__{name}></api__{name}>` tags that will be prependet to the parameter name.
- `deprecated` - Adds a notice about the deprecation of this Constructor in the form of a deprecation label and a `deprecated` section with the provided explanation.
- `throws` - List of possible throws to display.
    - `name` - Name of the throw to display.
    - `description` - Description of the throw.
- `seealso` - List of "See also" text to display.
    - `name` - Name of the See also link.
    - `link` - Link (Relative, absolute or URL) to use for the See also text.

### Nested Classes

```yaml
classes:
  - name: '<string>'
    description: '<string>'
    type: '<string>'
    link: '<string>'
```

**Options:**

- `name` - The name to display for the Nested Classes Summary.
- `description` - The description of the nested class.
- `type` - Modifier and type of the nested class.
- `link` - Link to the nested class.

### Enum Constants

```yaml
enums:
  - name: '<string>'
    description: '<string>'
    type: '<string>'
    deprecated: '<string>'
    seealso:
      - name: '<string>'
        link: '<string>'
```

**Options:**

- `name` - The name to display for the Constructor summary and detail. This should include any parameters the Constructor has, but without any parameter name.
- `description` - The description to display for the Constructor.
- `type` - The return type to display. Will default to `{{ page.title }}` if not set.
- `deprecated` - Adds a notice about the deprecation of this Enum in the form of a deprecation label and a `deprecated` section with the provided explanation.
- `seealso` - List of "See also" text to display.
    - `name` - Name of the See also link.
    - `link` - Link (Relative, absolute or URL) to use for the See also text.

### Methods

```yaml
methods:
  - name: '<string>'
    description: '<string>'
    type:
      name: '<string>'
      type: '<string>'
      link: '<string>'
    attributes:
      - '<string>'
    parameters:
      - name: '<string>'
        description: '<string>'
        type: '<string>'
        attributes:
          - '<string>'
    returns: '<string>'
    deprecated: '<string>'
    throws:
      - name: '<string>'
        description: '<string>'
    seealos:
      - name: '<string>'
        link: '<string>'
```

**Options:**

- `name` - The name to display for the Constructor summary and detail. This should include any parameters the Constructor has, but without any parameter name.
- `description` - The description to display for the Constructor.
- `type` - Options for object type to display.
    - `name` - Displayed name of the object.
    - `type` - Object type. Can be either `object` or `primitive` which would be used in a class name for styling.
    - `link` - Link (Relative, absolute or URL) to use for the type name.
- `attributes` - List of attributes (i.e. nullability) to display. The provided values will be turned into `<api__{name}></api__{name}>` tags that will be prependet to the name.
- `parameters` - List of parameters to display.
    - `name` - Name of the parameter to display.
    - `description` - Description of the parameter.
    - `type` - The type this parameter is.
    - `attributes` - List of attributes (i.e. nullability) to display. The provided values will be turned into `<api__{name}></api__{name}>` tags that will be prependet to the parameter name.
- `returns` - Possible return values.
- `deprecated` - Adds a notice about the deprecation of this Method in the form of a deprecation label and a `deprecated` section with the provided explanation.
- `throws` - List of possible throws to display.
    - `name` - Name of the throw to display.
    - `description` - Description of the throw.
- `seealso` - List of "See also" text to display.
    - `name` - Name of the See also link.
    - `link` - Link (Relative, absolute or URL) to use for the See also text.

### Inherits

```yaml
inherits:
  '<string>':
    link: '<string>'
    methods:
      - '<string>'
```

**Options:**

- `inherits.<string>` - Classpath used to display in the text `Methods inherited from <string>`
- `link` - Link to use for the classpath text.
- `methods` - List of strings to display. Each will be linked using `{link}#{method}`