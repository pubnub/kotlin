//[pubnub-kotlin](../../../index.md)/[com.pubnub.api.models.consumer.pubsub.objects](../index.md)/[PNDeleteChannelMetadataEventMessage](index.md)

# PNDeleteChannelMetadataEventMessage

[jvm]\
data class [PNDeleteChannelMetadataEventMessage](index.md)(val source: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), val version: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), val event: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), val type: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), val channel: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)) : [PNObjectEventMessage](../-p-n-object-event-message/index.md)

## Constructors

| | |
|---|---|
| [PNDeleteChannelMetadataEventMessage](-p-n-delete-channel-metadata-event-message.md) | [jvm]<br>fun [PNDeleteChannelMetadataEventMessage](-p-n-delete-channel-metadata-event-message.md)(source: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), version: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), event: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), type: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), channel: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)) |

## Properties

| Name | Summary |
|---|---|
| [channel](channel.md) | [jvm]<br>@SerializedName(value = &quot;data&quot;)<br>val [channel](channel.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [event](event.md) | [jvm]<br>open override val [event](event.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [source](source.md) | [jvm]<br>open override val [source](source.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [type](type.md) | [jvm]<br>open override val [type](type.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [version](version.md) | [jvm]<br>open override val [version](version.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
