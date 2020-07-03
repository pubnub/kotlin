---
title: MapperManager - pubnub-kotlin
---

[pubnub-kotlin](../../index.html) / [com.pubnub.api.managers](../index.html) / [MapperManager](./index.html)

# MapperManager

`class MapperManager`

### Constructors

| [&lt;init&gt;](-init-.html) | `MapperManager()` |

### Functions

| [convertValue](convert-value.html) | `fun <T> convertValue(input: JsonElement?, clazz: `[`Class`](https://docs.oracle.com/javase/6/docs/api/java/lang/Class.html)`<T>): T`<br>`fun <T> convertValue(o: `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?, clazz: `[`Class`](https://docs.oracle.com/javase/6/docs/api/java/lang/Class.html)`<T>?): T` |
| [elementToInt](element-to-int.html) | `fun elementToInt(element: JsonElement, field: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [elementToLong](element-to-long.html) | `fun elementToLong(element: JsonElement): `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)<br>`fun elementToLong(element: JsonElement, field: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html) |
| [elementToString](element-to-string.html) | `fun elementToString(element: JsonElement?): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?`<br>`fun elementToString(element: JsonElement?, field: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
| [fromJson](from-json.html) | `fun <T> fromJson(input: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?, clazz: `[`Class`](https://docs.oracle.com/javase/6/docs/api/java/lang/Class.html)`<T>): T`<br>`fun <T> fromJson(input: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?, typeOfT: `[`Type`](https://docs.oracle.com/javase/6/docs/api/java/lang/reflect/Type.html)`): T` |
| [getArrayElement](get-array-element.html) | `fun getArrayElement(element: JsonElement, index: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): JsonElement!` |
| [getArrayIterator](get-array-iterator.html) | `fun getArrayIterator(element: JsonElement?): `[`MutableIterator`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-iterator/index.html)`<JsonElement!>?`<br>`fun getArrayIterator(element: JsonElement, field: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`MutableIterator`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-iterator/index.html)`<JsonElement!>` |
| [getAsArray](get-as-array.html) | `fun getAsArray(element: JsonElement): JsonArray!` |
| [getAsBoolean](get-as-boolean.html) | `fun getAsBoolean(element: JsonElement, field: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [getAsObject](get-as-object.html) | `fun getAsObject(element: JsonElement): JsonObject!` |
| [getField](get-field.html) | `fun getField(element: JsonElement?, field: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): JsonElement?` |
| [getObjectIterator](get-object-iterator.html) | `fun getObjectIterator(element: JsonElement): `[`MutableIterator`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-iterator/index.html)`<`[`MutableEntry`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-map/-mutable-entry/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`!, JsonElement!>!>`<br>`fun getObjectIterator(element: JsonElement, field: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`MutableIterator`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-iterator/index.html)`<`[`MutableEntry`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-map/-mutable-entry/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`!, JsonElement!>!>` |
| [hasField](has-field.html) | `fun hasField(element: JsonElement, field: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [isJsonObject](is-json-object.html) | `fun isJsonObject(element: JsonElement): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [putOnObject](put-on-object.html) | `fun putOnObject(element: JsonObject, key: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, value: JsonElement): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [toJson](to-json.html) | `fun toJson(input: `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |

