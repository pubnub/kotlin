//[pubnub-kotlin-api](../../../index.md)/[com.pubnub.api.models.consumer.objects.membership](../index.md)/[PNChannelMembership](index.md)

# PNChannelMembership

[common]\
data class [PNChannelMembership](index.md)(val channel: [PNChannelMetadata](../../com.pubnub.api.models.consumer.objects.channel/-p-n-channel-metadata/index.md), val custom: [PatchValue](../../com.pubnub.api.utils/-patch-value/index.md)&lt;[Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)?&gt;?&gt;? = null, val updated: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), val eTag: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), val status: [PatchValue](../../com.pubnub.api.utils/-patch-value/index.md)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?&gt;? = null)

## Constructors

| | |
|---|---|
| [PNChannelMembership](-p-n-channel-membership.md) | [common]<br>constructor(channel: [PNChannelMetadata](../../com.pubnub.api.models.consumer.objects.channel/-p-n-channel-metadata/index.md), custom: [PatchValue](../../com.pubnub.api.utils/-patch-value/index.md)&lt;[Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)?&gt;?&gt;? = null, updated: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), eTag: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), status: [PatchValue](../../com.pubnub.api.utils/-patch-value/index.md)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?&gt;? = null) |

## Types

| Name | Summary |
|---|---|
| [Partial](-partial/index.md) | [common]<br>data class [Partial](-partial/index.md)(val channelId: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), val custom: [CustomObject](../../com.pubnub.kmp/-custom-object/index.md)? = null, val status: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = null) : [ChannelMembershipInput](../-channel-membership-input/index.md) |

## Properties

| Name | Summary |
|---|---|
| [channel](channel.md) | [common]<br>val [channel](channel.md): [PNChannelMetadata](../../com.pubnub.api.models.consumer.objects.channel/-p-n-channel-metadata/index.md) |
| [custom](custom.md) | [common]<br>val [custom](custom.md): [PatchValue](../../com.pubnub.api.utils/-patch-value/index.md)&lt;[Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)?&gt;?&gt;? = null |
| [eTag](e-tag.md) | [common]<br>val [eTag](e-tag.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [status](status.md) | [common]<br>val [status](status.md): [PatchValue](../../com.pubnub.api.utils/-patch-value/index.md)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?&gt;? = null |
| [updated](updated.md) | [common]<br>val [updated](updated.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |

## Functions

| Name | Summary |
|---|---|
| [plus](plus.md) | [common]<br>operator fun [plus](plus.md)(update: [PNSetMembershipEvent](../../com.pubnub.api.models.consumer.pubsub.objects/-p-n-set-membership-event/index.md)): [PNChannelMembership](index.md)<br>Merge information from this `PNChannelMembership` with new data from `update`, returning a new `PNChannelMembership` instance. |
