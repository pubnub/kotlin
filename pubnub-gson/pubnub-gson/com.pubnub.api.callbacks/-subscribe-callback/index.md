//[pubnub-gson](../../../index.md)/[com.pubnub.api.callbacks](../index.md)/[SubscribeCallback](index.md)

# SubscribeCallback

abstract class [SubscribeCallback](index.md) : [StatusListener](../../com.pubnub.api.v2.callbacks/-status-listener/index.md), [EventListener](../../com.pubnub.api.v2.callbacks/-event-listener/index.md)

#### Inheritors

| |
|---|
| [BaseSubscribeCallback](-base-subscribe-callback/index.md) |

## Constructors

| | |
|---|---|
| [SubscribeCallback](-subscribe-callback.md) | [jvm]<br>constructor() |

## Types

| Name | Summary |
|---|---|
| [BaseSubscribeCallback](-base-subscribe-callback/index.md) | [jvm]<br>open class [BaseSubscribeCallback](-base-subscribe-callback/index.md) : [SubscribeCallback](index.md) |

## Functions

| Name | Summary |
|---|---|
| [channel](../../com.pubnub.api.v2.callbacks/-event-listener/channel.md) | [jvm]<br>open fun [channel](../../com.pubnub.api.v2.callbacks/-event-listener/channel.md)(@NotNullpubnub: @NotNull[PubNub](../../com.pubnub.api/-pub-nub/index.md), @NotNullpnChannelMetadataResult: @NotNull[PNChannelMetadataResult](../../com.pubnub.api.models.consumer.objects_api.channel/-p-n-channel-metadata-result/index.md)) |
| [file](../../com.pubnub.api.v2.callbacks/-event-listener/file.md) | [jvm]<br>open fun [file](../../com.pubnub.api.v2.callbacks/-event-listener/file.md)(@NotNullpubnub: @NotNull[PubNub](../../com.pubnub.api/-pub-nub/index.md), @NotNullpnFileEventResult: @NotNull[PNFileEventResult](../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.models.consumer.pubsub.files/-p-n-file-event-result/index.md)) |
| [membership](../../com.pubnub.api.v2.callbacks/-event-listener/membership.md) | [jvm]<br>open fun [membership](../../com.pubnub.api.v2.callbacks/-event-listener/membership.md)(@NotNullpubnub: @NotNull[PubNub](../../com.pubnub.api/-pub-nub/index.md), @NotNullpnMembershipResult: @NotNull[PNMembershipResult](../../com.pubnub.api.models.consumer.objects_api.membership/-p-n-membership-result/index.md)) |
| [message](../../com.pubnub.api.v2.callbacks/-event-listener/message.md) | [jvm]<br>open fun [message](../../com.pubnub.api.v2.callbacks/-event-listener/message.md)(@NotNullpubnub: @NotNull[PubNub](../../com.pubnub.api/-pub-nub/index.md), @NotNullpnMessageResult: @NotNull[PNMessageResult](../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.models.consumer.pubsub/-p-n-message-result/index.md)) |
| [messageAction](../../com.pubnub.api.v2.callbacks/-event-listener/message-action.md) | [jvm]<br>open fun [messageAction](../../com.pubnub.api.v2.callbacks/-event-listener/message-action.md)(@NotNullpubnub: @NotNull[PubNub](../../com.pubnub.api/-pub-nub/index.md), @NotNullpnMessageActionResult: @NotNull[PNMessageActionResult](../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.models.consumer.pubsub.message_actions/-p-n-message-action-result/index.md)) |
| [presence](../../com.pubnub.api.v2.callbacks/-event-listener/presence.md) | [jvm]<br>open fun [presence](../../com.pubnub.api.v2.callbacks/-event-listener/presence.md)(@NotNullpubnub: @NotNull[PubNub](../../com.pubnub.api/-pub-nub/index.md), @NotNullpnPresenceEventResult: @NotNull[PNPresenceEventResult](../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.models.consumer.pubsub/-p-n-presence-event-result/index.md)) |
| [signal](../../com.pubnub.api.v2.callbacks/-event-listener/signal.md) | [jvm]<br>open fun [signal](../../com.pubnub.api.v2.callbacks/-event-listener/signal.md)(@NotNullpubnub: @NotNull[PubNub](../../com.pubnub.api/-pub-nub/index.md), @NotNullpnSignalResult: @NotNull[PNSignalResult](../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.models.consumer.pubsub/-p-n-signal-result/index.md)) |
| [status](../../com.pubnub.api.v2.callbacks/-status-listener/status.md) | [jvm]<br>abstract fun [status](../../com.pubnub.api.v2.callbacks/-status-listener/status.md)(@NotNullpubnub: @NotNull[PubNub](../../com.pubnub.api/-pub-nub/index.md), @NotNullpnStatus: @NotNull[PNStatus](../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.models.consumer/-p-n-status/index.md)) |
| [uuid](../../com.pubnub.api.v2.callbacks/-event-listener/uuid.md) | [jvm]<br>open fun [uuid](../../com.pubnub.api.v2.callbacks/-event-listener/uuid.md)(@NotNullpubnub: @NotNull[PubNub](../../com.pubnub.api/-pub-nub/index.md), @NotNullpnUUIDMetadataResult: @NotNull[PNUUIDMetadataResult](../../com.pubnub.api.models.consumer.objects_api.uuid/-p-n-u-u-i-d-metadata-result/index.md)) |
