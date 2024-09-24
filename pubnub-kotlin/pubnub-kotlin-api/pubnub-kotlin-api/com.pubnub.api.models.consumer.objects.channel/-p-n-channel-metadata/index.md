//[pubnub-kotlin-api](../../../index.md)/[com.pubnub.api.models.consumer.objects.channel](../index.md)/[PNChannelMetadata](index.md)

# PNChannelMetadata

[common]\
data class [PNChannelMetadata](index.md)(val id: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), val name: [PatchValue](../../com.pubnub.api.utils/-patch-value/index.md)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?&gt;? = null, val description: [PatchValue](../../com.pubnub.api.utils/-patch-value/index.md)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?&gt;? = null, val custom: [PatchValue](../../com.pubnub.api.utils/-patch-value/index.md)&lt;[Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)?&gt;?&gt;? = null, val updated: [PatchValue](../../com.pubnub.api.utils/-patch-value/index.md)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;? = null, val eTag: [PatchValue](../../com.pubnub.api.utils/-patch-value/index.md)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;? = null, val type: [PatchValue](../../com.pubnub.api.utils/-patch-value/index.md)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?&gt;? = null, val status: [PatchValue](../../com.pubnub.api.utils/-patch-value/index.md)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?&gt;? = null)

## Constructors

| | |
|---|---|
| [PNChannelMetadata](-p-n-channel-metadata.md) | [common]<br>constructor(id: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), name: [PatchValue](../../com.pubnub.api.utils/-patch-value/index.md)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?&gt;? = null, description: [PatchValue](../../com.pubnub.api.utils/-patch-value/index.md)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?&gt;? = null, custom: [PatchValue](../../com.pubnub.api.utils/-patch-value/index.md)&lt;[Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)?&gt;?&gt;? = null, updated: [PatchValue](../../com.pubnub.api.utils/-patch-value/index.md)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;? = null, eTag: [PatchValue](../../com.pubnub.api.utils/-patch-value/index.md)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;? = null, type: [PatchValue](../../com.pubnub.api.utils/-patch-value/index.md)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?&gt;? = null, status: [PatchValue](../../com.pubnub.api.utils/-patch-value/index.md)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?&gt;? = null) |

## Properties

| Name | Summary |
|---|---|
| [custom](custom.md) | [common]<br>val [custom](custom.md): [PatchValue](../../com.pubnub.api.utils/-patch-value/index.md)&lt;[Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)?&gt;?&gt;? = null |
| [description](description.md) | [common]<br>val [description](description.md): [PatchValue](../../com.pubnub.api.utils/-patch-value/index.md)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?&gt;? = null |
| [eTag](e-tag.md) | [common]<br>val [eTag](e-tag.md): [PatchValue](../../com.pubnub.api.utils/-patch-value/index.md)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;? = null |
| [id](id.md) | [common]<br>val [id](id.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [name](name.md) | [common]<br>val [name](name.md): [PatchValue](../../com.pubnub.api.utils/-patch-value/index.md)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?&gt;? = null |
| [status](status.md) | [common]<br>val [status](status.md): [PatchValue](../../com.pubnub.api.utils/-patch-value/index.md)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?&gt;? = null |
| [type](type.md) | [common]<br>val [type](type.md): [PatchValue](../../com.pubnub.api.utils/-patch-value/index.md)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?&gt;? = null |
| [updated](updated.md) | [common]<br>val [updated](updated.md): [PatchValue](../../com.pubnub.api.utils/-patch-value/index.md)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;? = null |

## Functions

| Name | Summary |
|---|---|
| [plus](plus.md) | [common]<br>operator fun [plus](plus.md)(update: [PNChannelMetadata](index.md)): [PNChannelMetadata](index.md)<br>Merge information from this `PNChannelMetadata` with new data from `update`, returning a new `PNChannelMetadata` instance. |
