//[pubnub-gson-api](../../../index.md)/[com.pubnub.api.java.callbacks](../index.md)/[SubscribeCallback](index.md)

# SubscribeCallback

abstract class [SubscribeCallback](index.md) : [StatusListener](../../com.pubnub.api.java.v2.callbacks/-status-listener/index.md), [EventListener](../../com.pubnub.api.java.v2.callbacks/-event-listener/index.md)

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
| [BaseSubscribeCallback](-base-subscribe-callback/index.md) | [jvm]<br>open class [BaseSubscribeCallback](-base-subscribe-callback/index.md) : [SubscribeCallback](index.md), [EventListener](../../com.pubnub.api.java.v2.callbacks/-event-listener/index.md) |

## Functions

| Name | Summary |
|---|---|
| [channel](../../com.pubnub.api.java.v2.callbacks/-event-listener/channel.md) | [jvm]<br>open fun [channel](../../com.pubnub.api.java.v2.callbacks/-event-listener/channel.md)(pubnub: [PubNub](../../com.pubnub.api.java/-pub-nub/index.md), pnChannelMetadataResult: [PNChannelMetadataResult](../../com.pubnub.api.java.models.consumer.objects_api.channel/-p-n-channel-metadata-result/index.md)) |
| [file](../../com.pubnub.api.java.v2.callbacks/-event-listener/file.md) | [jvm]<br>open fun [file](../../com.pubnub.api.java.v2.callbacks/-event-listener/file.md)(pubnub: [PubNub](../../com.pubnub.api.java/-pub-nub/index.md), result: [PNFileEventResult](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.models.consumer.pubsub.files/-p-n-file-event-result/index.md)) |
| [membership](../../com.pubnub.api.java.v2.callbacks/-event-listener/membership.md) | [jvm]<br>open fun [membership](../../com.pubnub.api.java.v2.callbacks/-event-listener/membership.md)(pubnub: [PubNub](../../com.pubnub.api.java/-pub-nub/index.md), pnMembershipResult: [PNMembershipResult](../../com.pubnub.api.java.models.consumer.objects_api.membership/-p-n-membership-result/index.md)) |
| [message](../../com.pubnub.api.java.v2.callbacks/-event-listener/message.md) | [jvm]<br>open fun [message](../../com.pubnub.api.java.v2.callbacks/-event-listener/message.md)(pubnub: [PubNub](../../com.pubnub.api.java/-pub-nub/index.md), result: [PNMessageResult](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.models.consumer.pubsub/-p-n-message-result/index.md)) |
| [messageAction](../../com.pubnub.api.java.v2.callbacks/-event-listener/message-action.md) | [jvm]<br>open fun [messageAction](../../com.pubnub.api.java.v2.callbacks/-event-listener/message-action.md)(pubNub: [PubNub](../../com.pubnub.api.java/-pub-nub/index.md), result: [PNMessageActionResult](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.models.consumer.pubsub.message_actions/-p-n-message-action-result/index.md)) |
| [presence](../../com.pubnub.api.java.v2.callbacks/-event-listener/presence.md) | [jvm]<br>open fun [presence](../../com.pubnub.api.java.v2.callbacks/-event-listener/presence.md)(pubnub: [PubNub](../../com.pubnub.api.java/-pub-nub/index.md), result: [PNPresenceEventResult](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.models.consumer.pubsub/-p-n-presence-event-result/index.md)) |
| [signal](../../com.pubnub.api.java.v2.callbacks/-event-listener/signal.md) | [jvm]<br>open fun [signal](../../com.pubnub.api.java.v2.callbacks/-event-listener/signal.md)(pubnub: [PubNub](../../com.pubnub.api.java/-pub-nub/index.md), result: [PNSignalResult](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.models.consumer.pubsub/-p-n-signal-result/index.md)) |
| [status](../../com.pubnub.api.java.v2.callbacks/-status-listener/status.md) | [jvm]<br>abstract fun [status](../../com.pubnub.api.java.v2.callbacks/-status-listener/status.md)(pubnub: [PubNub](../../com.pubnub.api.java/-pub-nub/index.md), status: [PNStatus](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.models.consumer/-p-n-status/index.md))<br>Receive status updates from the PubNub client, such as: |
| [uuid](../../com.pubnub.api.java.v2.callbacks/-event-listener/uuid.md) | [jvm]<br>open fun [uuid](../../com.pubnub.api.java.v2.callbacks/-event-listener/uuid.md)(pubnub: [PubNub](../../com.pubnub.api.java/-pub-nub/index.md), pnUUIDMetadataResult: [PNUUIDMetadataResult](../../com.pubnub.api.java.models.consumer.objects_api.uuid/-p-n-u-u-i-d-metadata-result/index.md)) |
