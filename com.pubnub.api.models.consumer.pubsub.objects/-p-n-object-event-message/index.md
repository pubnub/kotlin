[pubnub-kotlin](../../index.md) / [com.pubnub.api.models.consumer.pubsub.objects](../index.md) / [PNObjectEventMessage](./index.md)

# PNObjectEventMessage

`sealed class PNObjectEventMessage`

### Properties

| Name | Summary |
|---|---|
| [event](event.md) | `abstract val event: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [source](source.md) | `abstract val source: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [type](type.md) | `abstract val type: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [version](version.md) | `abstract val version: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |

### Inheritors

| Name | Summary |
|---|---|
| [PNDeleteChannelMetadataEventMessage](../-p-n-delete-channel-metadata-event-message/index.md) | `data class PNDeleteChannelMetadataEventMessage : `[`PNObjectEventMessage`](./index.md) |
| [PNDeleteMembershipEventMessage](../-p-n-delete-membership-event-message/index.md) | `data class PNDeleteMembershipEventMessage : `[`PNObjectEventMessage`](./index.md) |
| [PNDeleteUUIDMetadataEventMessage](../-p-n-delete-u-u-i-d-metadata-event-message/index.md) | `data class PNDeleteUUIDMetadataEventMessage : `[`PNObjectEventMessage`](./index.md) |
| [PNSetChannelMetadataEventMessage](../-p-n-set-channel-metadata-event-message/index.md) | `data class PNSetChannelMetadataEventMessage : `[`PNObjectEventMessage`](./index.md) |
| [PNSetMembershipEventMessage](../-p-n-set-membership-event-message/index.md) | `data class PNSetMembershipEventMessage : `[`PNObjectEventMessage`](./index.md) |
| [PNSetUUIDMetadataEventMessage](../-p-n-set-u-u-i-d-metadata-event-message/index.md) | `data class PNSetUUIDMetadataEventMessage : `[`PNObjectEventMessage`](./index.md) |
