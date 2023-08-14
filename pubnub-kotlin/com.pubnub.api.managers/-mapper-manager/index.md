//[pubnub-kotlin](../../../index.md)/[com.pubnub.api.managers](../index.md)/[MapperManager](index.md)

# MapperManager

[jvm]\
class [MapperManager](index.md)

## Constructors

| | |
|---|---|
| [MapperManager](-mapper-manager.md) | [jvm]<br>fun [MapperManager](-mapper-manager.md)() |

## Functions

| Name | Summary |
|---|---|
| [convertValue](convert-value.md) | [jvm]<br>fun &lt;[T](convert-value.md)&gt; [convertValue](convert-value.md)(input: JsonElement?, clazz: [Class](https://docs.oracle.com/javase/8/docs/api/java/lang/Class.html)&lt;[T](convert-value.md)&gt;): [T](convert-value.md)<br>fun &lt;[T](convert-value.md)&gt; [convertValue](convert-value.md)(o: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)?, clazz: [Class](https://docs.oracle.com/javase/8/docs/api/java/lang/Class.html)&lt;[T](convert-value.md)&gt;?): [T](convert-value.md) |
| [elementToInt](element-to-int.md) | [jvm]<br>fun [elementToInt](element-to-int.md)(element: JsonElement, field: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [elementToLong](element-to-long.md) | [jvm]<br>fun [elementToLong](element-to-long.md)(element: JsonElement): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)<br>fun [elementToLong](element-to-long.md)(element: JsonElement, field: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html) |
| [elementToString](element-to-string.md) | [jvm]<br>fun [elementToString](element-to-string.md)(element: JsonElement?): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?<br>fun [elementToString](element-to-string.md)(element: JsonElement?, field: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? |
| [fromJson](from-json.md) | [jvm]<br>fun &lt;[T](from-json.md)&gt; [fromJson](from-json.md)(input: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?, clazz: [Class](https://docs.oracle.com/javase/8/docs/api/java/lang/Class.html)&lt;[T](from-json.md)&gt;): [T](from-json.md)<br>fun &lt;[T](from-json.md)&gt; [fromJson](from-json.md)(input: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?, typeOfT: [Type](https://docs.oracle.com/javase/8/docs/api/java/lang/reflect/Type.html)): [T](from-json.md) |
| [getArrayElement](get-array-element.md) | [jvm]<br>fun [getArrayElement](get-array-element.md)(element: JsonElement, index: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): JsonElement |
| [getArrayIterator](get-array-iterator.md) | [jvm]<br>fun [getArrayIterator](get-array-iterator.md)(element: JsonElement?): [MutableIterator](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-iterator/index.html)&lt;JsonElement&gt;?<br>fun [getArrayIterator](get-array-iterator.md)(element: JsonElement, field: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [MutableIterator](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-iterator/index.html)&lt;JsonElement&gt; |
| [getAsArray](get-as-array.md) | [jvm]<br>fun [getAsArray](get-as-array.md)(element: JsonElement): JsonArray |
| [getAsBoolean](get-as-boolean.md) | [jvm]<br>fun [getAsBoolean](get-as-boolean.md)(element: JsonElement, field: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [getAsObject](get-as-object.md) | [jvm]<br>fun [getAsObject](get-as-object.md)(element: JsonElement): JsonObject |
| [getField](get-field.md) | [jvm]<br>fun [getField](get-field.md)(element: JsonElement?, field: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): JsonElement? |
| [getObjectIterator](get-object-iterator.md) | [jvm]<br>fun [getObjectIterator](get-object-iterator.md)(element: JsonElement): [MutableIterator](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-iterator/index.html)&lt;[MutableMap.MutableEntry](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-map/-mutable-entry/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), JsonElement&gt;&gt;<br>fun [getObjectIterator](get-object-iterator.md)(element: JsonElement, field: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [MutableIterator](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-iterator/index.html)&lt;[MutableMap.MutableEntry](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-map/-mutable-entry/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), JsonElement&gt;&gt; |
| [hasField](has-field.md) | [jvm]<br>fun [hasField](has-field.md)(element: JsonElement, field: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [isJsonObject](is-json-object.md) | [jvm]<br>fun [isJsonObject](is-json-object.md)(element: JsonElement): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [putOnObject](put-on-object.md) | [jvm]<br>fun [putOnObject](put-on-object.md)(element: JsonObject, key: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), value: JsonElement) |
| [toJson](to-json.md) | [jvm]<br>fun [toJson](to-json.md)(input: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)?): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [toJsonTree](to-json-tree.md) | [jvm]<br>fun [toJsonTree](to-json-tree.md)(any: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)?): JsonElement |
