//[pubnub-kotlin-core-api](../../../index.md)/[com.pubnub.api](../index.md)/[JsonElement](index.md)

# JsonElement

expect abstract class [JsonElement](index.md)actual abstract class [JsonElement](index.md)actual abstract class [JsonElement](index.md)actual typealias [JsonElement](index.md) = com.google.gson.JsonElement

#### Inheritors

| |
|---|
| [JsonElementImpl](../[apple]-json-element-impl/index.md) |
| [JsonElementImpl](../[js]-json-element-impl/index.md) |

## Constructors

| | |
|---|---|
| [JsonElement](-json-element.md) | [apple, js]<br>constructor(value: [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)?) |

## Properties

| Name | Summary |
|---|---|
| [value](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api/-json-element/[js]value.md) | [apple, js]<br>[apple]<br>val [value]([apple]value.md): [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)?<br>[js]<br>val [value]([js]value.md): [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)? |

## Functions

| Name | Summary |
|---|---|
| [asBoolean](../as-boolean.md) | [jvm, common, apple, js]<br>[jvm, apple, js]<br>actual fun [JsonElement](index.md).[asBoolean](../as-boolean.md)(): [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html)?<br>[common]<br>expect fun [JsonElement](index.md).[asBoolean](../as-boolean.md)(): [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html)? |
| [asDouble](../as-double.md) | [jvm, common, apple, js]<br>[jvm, apple, js]<br>actual fun [JsonElement](index.md).[asDouble](../as-double.md)(): [Double](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-double/index.html)?<br>[common]<br>expect fun [JsonElement](index.md).[asDouble](../as-double.md)(): [Double](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-double/index.html)? |
| [asList](../as-list.md) | [jvm, common, apple, js]<br>[jvm, apple, js]<br>actual fun [JsonElement](index.md).[asList](../as-list.md)(): [List](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[JsonElement](index.md)&gt;?<br>[common]<br>expect fun [JsonElement](index.md).[asList](../as-list.md)(): [List](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[JsonElement](index.md)&gt;? |
| [asLong](../as-long.md) | [jvm, common, apple, js]<br>[jvm, apple, js]<br>actual fun [JsonElement](index.md).[asLong](../as-long.md)(): [Long](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-long/index.html)?<br>[common]<br>expect fun [JsonElement](index.md).[asLong](../as-long.md)(): [Long](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-long/index.html)? |
| [asMap](../as-map.md) | [jvm, common, apple, js]<br>[jvm, apple, js]<br>actual fun [JsonElement](index.md).[asMap](../as-map.md)(): [Map](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), [JsonElement](index.md)&gt;?<br>[common]<br>expect fun [JsonElement](index.md).[asMap](../as-map.md)(): [Map](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), [JsonElement](index.md)&gt;? |
| [asNumber](../as-number.md) | [jvm, common, apple, js]<br>[jvm, apple, js]<br>actual fun [JsonElement](index.md).[asNumber](../as-number.md)(): [Number](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-number/index.html)?<br>[common]<br>expect fun [JsonElement](index.md).[asNumber](../as-number.md)(): [Number](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-number/index.html)? |
| [asString](../as-string.md) | [jvm, common, apple, js]<br>[jvm, apple, js]<br>actual fun [JsonElement](index.md).[asString](../as-string.md)(): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)?<br>[common]<br>expect fun [JsonElement](index.md).[asString](../as-string.md)(): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)? |
| [decode](../decode.md) | [common]<br>fun [JsonElement](index.md).[decode](../decode.md)(): [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)? |
| [equals](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api/-json-element/[js]equals.md) | [apple, js]<br>[apple]<br>open operator override fun [equals]([apple]equals.md)(other: [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)?): [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html)<br>[js]<br>open operator override fun [equals]([js]equals.md)(other: [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)?): [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html) |
| [hashCode](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api/-json-element/[js]hash-code.md) | [apple, js]<br>[apple]<br>open override fun [hashCode]([apple]hash-code.md)(): [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html)<br>[js]<br>open override fun [hashCode]([js]hash-code.md)(): [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html) |
| [isNull](../is-null.md) | [jvm, common, apple, js]<br>[jvm, apple, js]<br>actual fun [JsonElement](index.md).[isNull](../is-null.md)(): [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html)<br>[common]<br>expect fun [JsonElement](index.md).[isNull](../is-null.md)(): [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html) |
| [toString](to-string.md) | [apple]<br>open override fun [toString](to-string.md)(): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html) |
