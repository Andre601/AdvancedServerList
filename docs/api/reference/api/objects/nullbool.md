---
template: api-doc.html

enums:
  - name: 'true'
    description: 'Boolean value <code>true</code>'
  - name: 'false'
    description: 'Boolean value <code>false</code>'
  - name: 'not_set'
    description: 'Boolean value <code>null</code>'

methods:
  - name: 'resolve'
    description: |
      Returns a NullBool instance based on the provided <code>Boolean</code> value.<br>
      In case of <code>null</code> being provided will <a href="#not_set"><code>NullBool.NOT_SET</code></a> be returned, otherwise will the corresponding NullBool instance matching the Boolean value be returned.
    returns: 'NullBool instance based on the provided <code>Boolean</code> value.'
    parameters:
      - name: 'bool'
        description: 'The <code>Boolean</code> value to receive a NullBool instance for.'
        type: Boolean
        attributes:
          - nullable
    attributes:
      - static
    type:
      name: 'NullBool'
      type: 'object'
  - name: 'isNotSet'
    description: 'Returns whether the NullBool is <a href="#not_set"><code>NullBool.NOT_SET</code></a>.'
    returns: 'True if the instance is <a href="#not_set"><code>NullBool.NOT_SET</code></a>, otherwise false.'
    type:
      name: 'boolean'
      type: 'primitive'
  - name: 'getOrDefault'
    description: |
      Gets the corresponding boolean value associated with the NullBool instance.<br>
      In the case of NullBool <a href="#isnotset()">not being set</a> will the provided default value be returned.
    returns: 'True or false depending on the NullBool instance and the provided default value.'
    parameters:
      - name: 'def'
        description: 'The default boolean value to return should the NullBool instance be <a href="#not_set"><code>NullBool.NOT_SET</code></a>.'
        type: boolean
    type:
      name: 'boolean'
      type: 'primitive'
---

# <api__enum></api__enum> NullBool

Enum used to return a `Boolean` value with a default one should no value be set.