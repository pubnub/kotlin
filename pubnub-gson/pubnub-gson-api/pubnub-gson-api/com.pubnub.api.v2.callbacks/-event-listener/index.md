//[pubnub-gson-api](../../../index.md)/[com.pubnub.api.v2.callbacks](../index.md)/[EventListener](index.md)

# EventListener

interface [EventListener](index.md) : [BaseEventListener](../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.v2.callbacks/-base-event-listener/index.md)

Implement this interface and pass it into [EventEmitter.addListener] to listen for events from the PubNub real-time network.

#### Inheritors

| |
|---|
| [SubscribeCallback](../../com.pubnub.api.callbacks/-subscribe-callback/index.md) |

## Functions

| Name | Summary |
|---|---|
| [channel](channel.md) | [jvm]<br>open fun [channel](channel.md)(@NotNullpubnub: @NotNull[PubNub](../../com.pubnub.api/-pub-nub/index.md), @NotNullpnChannelMetadataResult: @NotNull[PNChannelMetadataResult](../../com.pubnub.api.models.consumer.objects_api.channel/-p-n-channel-metadata-result/index.md)) |
| [file](file.md) | [jvm]<br>open fun [file](file.md)(@NotNullpubnub: @NotNull[PubNub](../../com.pubnub.api/-pub-nub/index.md), @NotNullpnFileEventResult: @NotNull[PNFileEventResult](../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.models.consumer.pubsub.files/-p-n-file-event-result/index.md)) |
| [membership](membership.md) | [jvm]<br>open fun [membership](membership.md)(@NotNullpubnub: @NotNull[PubNub](../../com.pubnub.api/-pub-nub/index.md), @NotNullpnMembershipResult: @NotNull[PNMembershipResult](../../com.pubnub.api.models.consumer.objects_api.membership/-p-n-membership-result/index.md)) |
| [message](message.md) | [jvm]<br>open fun [message](message.md)(@NotNullpubnub: @NotNull[PubNub](../../com.pubnub.api/-pub-nub/index.md), @NotNullpnMessageResult: @NotNull[PNMessageResult](../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.models.consumer.pubsub/-p-n-message-result/index.md)) |
| [messageAction](message-action.md) | [jvm]<br>open fun [messageAction](message-action.md)(@NotNullpubnub: @NotNull[PubNub](../../com.pubnub.api/-pub-nub/index.md), @NotNullpnMessageActionResult: @NotNull[PNMessageActionResult](../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.models.consumer.pubsub.message_actions/-p-n-message-action-result/index.md)) |
| [presence](presence.md) | [jvm]<br>open fun [presence](presence.md)(@NotNullpubnub: @NotNull[PubNub](../../com.pubnub.api/-pub-nub/index.md), @NotNullpnPresenceEventResult: @NotNull[PNPresenceEventResult](../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.models.consumer.pubsub/-p-n-presence-event-result/index.md)) |
| [signal](signal.md) | [jvm]<br>open fun [signal](signal.md)(@NotNullpubnub: @NotNull[PubNub](../../com.pubnub.api/-pub-nub/index.md), @NotNullpnSignalResult: @NotNull[PNSignalResult](../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.models.consumer.pubsub/-p-n-signal-result/index.md)) |
| [uuid](uuid.md) | [jvm]<br>open fun [uuid](uuid.md)(@NotNullpubnub: @NotNull[PubNub](../../com.pubnub.api/-pub-nub/index.md), @NotNullpnUUIDMetadataResult: @NotNull[PNUUIDMetadataResult](../../com.pubnub.api.models.consumer.objects_api.uuid/-p-n-u-u-i-d-metadata-result/index.md)) |
