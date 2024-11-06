//[pubnub-gson-api](../../../../index.md)/[com.pubnub.api.java.callbacks](../../index.md)/[SubscribeCallback](../index.md)/[BaseSubscribeCallback](index.md)

# BaseSubscribeCallback

[jvm]\
open class [BaseSubscribeCallback](index.md) : [SubscribeCallback](../index.md), [EventListener](../../../com.pubnub.api.java.v2.callbacks/-event-listener/index.md)

## Constructors

| | |
|---|---|
| [BaseSubscribeCallback](-base-subscribe-callback.md) | [jvm]<br>constructor() |

## Functions

| Name | Summary |
|---|---|
| [channel](../../../com.pubnub.api.java.v2.callbacks/-event-listener/channel.md) | [jvm]<br>open fun [channel](../../../com.pubnub.api.java.v2.callbacks/-event-listener/channel.md)(pubnub: [PubNub](../../../com.pubnub.api.java/-pub-nub/index.md), pnChannelMetadataResult: [PNChannelMetadataResult](../../../com.pubnub.api.java.models.consumer.objects_api.channel/-p-n-channel-metadata-result/index.md)) |
| [file](../../../com.pubnub.api.java.v2.callbacks/-event-listener/file.md) | [jvm]<br>open fun [file](../../../com.pubnub.api.java.v2.callbacks/-event-listener/file.md)(pubnub: [PubNub](../../../com.pubnub.api.java/-pub-nub/index.md), result: [PNFileEventResult](../../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.models.consumer.pubsub.files/-p-n-file-event-result/index.md)) |
| [membership](../../../com.pubnub.api.java.v2.callbacks/-event-listener/membership.md) | [jvm]<br>open fun [membership](../../../com.pubnub.api.java.v2.callbacks/-event-listener/membership.md)(pubnub: [PubNub](../../../com.pubnub.api.java/-pub-nub/index.md), pnMembershipResult: [PNMembershipResult](../../../com.pubnub.api.java.models.consumer.objects_api.membership/-p-n-membership-result/index.md)) |
| [message](../../../com.pubnub.api.java.v2.callbacks/-event-listener/message.md) | [jvm]<br>open fun [message](../../../com.pubnub.api.java.v2.callbacks/-event-listener/message.md)(pubnub: [PubNub](../../../com.pubnub.api.java/-pub-nub/index.md), result: [PNMessageResult](../../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.models.consumer.pubsub/-p-n-message-result/index.md)) |
| [messageAction](../../../com.pubnub.api.java.v2.callbacks/-event-listener/message-action.md) | [jvm]<br>open fun [messageAction](../../../com.pubnub.api.java.v2.callbacks/-event-listener/message-action.md)(pubNub: [PubNub](../../../com.pubnub.api.java/-pub-nub/index.md), result: [PNMessageActionResult](../../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.models.consumer.pubsub.message_actions/-p-n-message-action-result/index.md)) |
| [presence](../../../com.pubnub.api.java.v2.callbacks/-event-listener/presence.md) | [jvm]<br>open fun [presence](../../../com.pubnub.api.java.v2.callbacks/-event-listener/presence.md)(pubnub: [PubNub](../../../com.pubnub.api.java/-pub-nub/index.md), result: [PNPresenceEventResult](../../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.models.consumer.pubsub/-p-n-presence-event-result/index.md)) |
| [signal](../../../com.pubnub.api.java.v2.callbacks/-event-listener/signal.md) | [jvm]<br>open fun [signal](../../../com.pubnub.api.java.v2.callbacks/-event-listener/signal.md)(pubnub: [PubNub](../../../com.pubnub.api.java/-pub-nub/index.md), result: [PNSignalResult](../../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.models.consumer.pubsub/-p-n-signal-result/index.md)) |
| [status](status.md) | [jvm]<br>open override fun [status](status.md)(pubnub: [PubNub](../../../com.pubnub.api.java/-pub-nub/index.md), pnStatus: [PNStatus](../../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.models.consumer/-p-n-status/index.md))<br>Receive status updates from the PubNub client, such as: |
| [uuid](../../../com.pubnub.api.java.v2.callbacks/-event-listener/uuid.md) | [jvm]<br>open fun [uuid](../../../com.pubnub.api.java.v2.callbacks/-event-listener/uuid.md)(pubnub: [PubNub](../../../com.pubnub.api.java/-pub-nub/index.md), pnUUIDMetadataResult: [PNUUIDMetadataResult](../../../com.pubnub.api.java.models.consumer.objects_api.uuid/-p-n-u-u-i-d-metadata-result/index.md)) |
