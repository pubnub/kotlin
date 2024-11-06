//[pubnub-kotlin-core-api](../../../index.md)/[com.pubnub.api.models.consumer](../index.md)/[PNStatus](index.md)

# PNStatus

[common]\
class [PNStatus](index.md)(val category: [PNStatusCategory](../../com.pubnub.api.enums/-p-n-status-category/index.md), val exception: [PubNubException](../../com.pubnub.api/-pub-nub-exception/index.md)? = null, val currentTimetoken: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)? = null, val affectedChannels: [Collection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt; = emptySet(), val affectedChannelGroups: [Collection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt; = emptySet())

## Constructors

| | |
|---|---|
| [PNStatus](-p-n-status.md) | [common]<br>constructor(category: [PNStatusCategory](../../com.pubnub.api.enums/-p-n-status-category/index.md), exception: [PubNubException](../../com.pubnub.api/-pub-nub-exception/index.md)? = null, currentTimetoken: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)? = null, affectedChannels: [Collection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt; = emptySet(), affectedChannelGroups: [Collection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt; = emptySet()) |

## Properties

| Name | Summary |
|---|---|
| [affectedChannelGroups](affected-channel-groups.md) | [common]<br>val [affectedChannelGroups](affected-channel-groups.md): [Collection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt; |
| [affectedChannels](affected-channels.md) | [common]<br>val [affectedChannels](affected-channels.md): [Collection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt; |
| [category](category.md) | [common]<br>val [category](category.md): [PNStatusCategory](../../com.pubnub.api.enums/-p-n-status-category/index.md) |
| [currentTimetoken](current-timetoken.md) | [common]<br>val [currentTimetoken](current-timetoken.md): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)? = null |
| [error](error.md) | [common]<br>@get:[JvmName](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.jvm/-jvm-name/index.html)(name = &quot;isError&quot;)<br>val [error](error.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [exception](exception.md) | [common]<br>val [exception](exception.md): [PubNubException](../../com.pubnub.api/-pub-nub-exception/index.md)? = null |

## Functions

| Name | Summary |
|---|---|
| [equals](equals.md) | [common]<br>open operator override fun [equals](equals.md)(other: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)?): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [hashCode](hash-code.md) | [common]<br>open override fun [hashCode](hash-code.md)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [toString](to-string.md) | [common]<br>open override fun [toString](to-string.md)(): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
