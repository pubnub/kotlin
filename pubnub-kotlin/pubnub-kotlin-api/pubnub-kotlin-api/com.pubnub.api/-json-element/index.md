//[pubnub-kotlin-api](../../../index.md)/[com.pubnub.api](../index.md)/[JsonElement](index.md)

# JsonElement

expect abstract class [JsonElement](index.md)actual abstract class [JsonElement](index.md)(val value: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)?)actual typealias [JsonElement](index.md) = com.google.gson.JsonElement

#### Inheritors

| |
|---|
| [JsonElementImpl](../-json-element-impl/index.md) |

## Constructors

| | |
|---|---|
| [JsonElement](-json-element.md) | [ios]<br>constructor(value: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)?) |

## Properties

| Name | Summary |
|---|---|
| [value](value.md) | [ios]<br>val [value](value.md): [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)? |

## Functions

| Name | Summary |
|---|---|
| [asBoolean](../as-boolean.md) | [common, ios]<br>[common]<br>expect fun [JsonElement](index.md).[asBoolean](../as-boolean.md)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)?<br>[ios]<br>actual fun [JsonElement](index.md).[asBoolean](../as-boolean.md)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)? |
| [asDouble](../as-double.md) | [common, ios]<br>[common]<br>expect fun [JsonElement](index.md).[asDouble](../as-double.md)(): [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html)?<br>[ios]<br>actual fun [JsonElement](index.md).[asDouble](../as-double.md)(): [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html)? |
| [asList](../as-list.md) | [common, ios]<br>[common]<br>expect fun [JsonElement](index.md).[asList](../as-list.md)(): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[JsonElement](index.md)&gt;?<br>[ios]<br>actual fun [JsonElement](index.md).[asList](../as-list.md)(): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[JsonElement](index.md)&gt;? |
| [asLong](../as-long.md) | [common, ios]<br>[common]<br>expect fun [JsonElement](index.md).[asLong](../as-long.md)(): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)?<br>[ios]<br>actual fun [JsonElement](index.md).[asLong](../as-long.md)(): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)? |
| [asMap](../as-map.md) | [common, ios]<br>[common]<br>expect fun [JsonElement](index.md).[asMap](../as-map.md)(): [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [JsonElement](index.md)&gt;?<br>[ios]<br>actual fun [JsonElement](index.md).[asMap](../as-map.md)(): [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [JsonElement](index.md)&gt;? |
| [asNumber](../as-number.md) | [common, ios]<br>[common]<br>expect fun [JsonElement](index.md).[asNumber](../as-number.md)(): [Number](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-number/index.html)?<br>[ios]<br>actual fun [JsonElement](index.md).[asNumber](../as-number.md)(): [Number](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-number/index.html)? |
| [asString](../as-string.md) | [common, ios]<br>[common]<br>expect fun [JsonElement](index.md).[asString](../as-string.md)(): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?<br>[ios]<br>actual fun [JsonElement](index.md).[asString](../as-string.md)(): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? |
| [decode](../decode.md) | [common]<br>fun [JsonElement](index.md).[decode](../decode.md)(): [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)? |
| [equals](equals.md) | [ios]<br>open operator override fun [equals](equals.md)(other: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)?): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [hashCode](hash-code.md) | [ios]<br>open override fun [hashCode](hash-code.md)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [isNull](../is-null.md) | [common, ios]<br>[common]<br>expect fun [JsonElement](index.md).[isNull](../is-null.md)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>[ios]<br>actual fun [JsonElement](index.md).[isNull](../is-null.md)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [toString](to-string.md) | [ios]<br>open override fun [toString](to-string.md)(): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
