//[pubnub-kotlin](../../../index.md)/[com.pubnub.api.models.consumer.pubsub.objects](../index.md)/[PNDeleteUUIDMetadataEventMessage](index.md)

# PNDeleteUUIDMetadataEventMessage

[jvm]\
data class [PNDeleteUUIDMetadataEventMessage](index.md)(val source: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), val version: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), val event: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), val type: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), val uuid: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)) : [PNObjectEventMessage](../-p-n-object-event-message/index.md)

## Constructors

| | |
|---|---|
| [PNDeleteUUIDMetadataEventMessage](-p-n-delete-u-u-i-d-metadata-event-message.md) | [jvm]<br>fun [PNDeleteUUIDMetadataEventMessage](-p-n-delete-u-u-i-d-metadata-event-message.md)(source: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), version: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), event: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), type: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), uuid: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)) |

## Properties

| Name | Summary |
|---|---|
| [event](event.md) | [jvm]<br>open override val [event](event.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [source](source.md) | [jvm]<br>open override val [source](source.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [type](type.md) | [jvm]<br>open override val [type](type.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [uuid](uuid.md) | [jvm]<br>@SerializedName(value = &quot;data&quot;)<br>val [uuid](uuid.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [version](version.md) | [jvm]<br>open override val [version](version.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
