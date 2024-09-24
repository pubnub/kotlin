//[pubnub-gson-api](../../../index.md)/[com.pubnub.api.java.v2.callbacks](../index.md)/[EventListener](index.md)

# EventListener

interface [EventListener](index.md) : [Listener](../../../../../pubnub-kotlin/pubnub-kotlin-api/pubnub-kotlin-api/com.pubnub.api.callbacks/-listener/index.md)

Implement this interface and pass it into [EventEmitter.addListener](../-event-emitter/add-listener.md) to listen for events from the PubNub real-time network.

#### Inheritors

| |
|---|
| [SubscribeCallback](../../com.pubnub.api.java.callbacks/-subscribe-callback/index.md) |
| [BaseSubscribeCallback](../../com.pubnub.api.java.callbacks/-subscribe-callback/-base-subscribe-callback/index.md) |

## Functions

| Name | Summary |
|---|---|
| [channel](channel.md) | [jvm]<br>open fun [channel](channel.md)(pubnub: [PubNub](../../com.pubnub.api.java/-pub-nub/index.md), pnChannelMetadataResult: [PNChannelMetadataResult](../../com.pubnub.api.java.models.consumer.objects_api.channel/-p-n-channel-metadata-result/index.md)) |
| [file](file.md) | [jvm]<br>open fun [file](file.md)(pubnub: [PubNub](../../com.pubnub.api.java/-pub-nub/index.md), result: [PNFileEventResult](../../../../../pubnub-kotlin/pubnub-kotlin-api/pubnub-kotlin-api/com.pubnub.api.models.consumer.pubsub.files/-p-n-file-event-result/index.md)) |
| [membership](membership.md) | [jvm]<br>open fun [membership](membership.md)(pubnub: [PubNub](../../com.pubnub.api.java/-pub-nub/index.md), pnMembershipResult: [PNMembershipResult](../../com.pubnub.api.java.models.consumer.objects_api.membership/-p-n-membership-result/index.md)) |
| [message](message.md) | [jvm]<br>open fun [message](message.md)(pubnub: [PubNub](../../com.pubnub.api.java/-pub-nub/index.md), result: [PNMessageResult](../../../../../pubnub-kotlin/pubnub-kotlin-api/pubnub-kotlin-api/com.pubnub.api.models.consumer.pubsub/-p-n-message-result/index.md)) |
| [messageAction](message-action.md) | [jvm]<br>open fun [messageAction](message-action.md)(pubNub: [PubNub](../../com.pubnub.api.java/-pub-nub/index.md), result: [PNMessageActionResult](../../../../../pubnub-kotlin/pubnub-kotlin-api/pubnub-kotlin-api/com.pubnub.api.models.consumer.pubsub.message_actions/-p-n-message-action-result/index.md)) |
| [presence](presence.md) | [jvm]<br>open fun [presence](presence.md)(pubnub: [PubNub](../../com.pubnub.api.java/-pub-nub/index.md), result: [PNPresenceEventResult](../../../../../pubnub-kotlin/pubnub-kotlin-api/pubnub-kotlin-api/com.pubnub.api.models.consumer.pubsub/-p-n-presence-event-result/index.md)) |
| [signal](signal.md) | [jvm]<br>open fun [signal](signal.md)(pubnub: [PubNub](../../com.pubnub.api.java/-pub-nub/index.md), result: [PNSignalResult](../../../../../pubnub-kotlin/pubnub-kotlin-api/pubnub-kotlin-api/com.pubnub.api.models.consumer.pubsub/-p-n-signal-result/index.md)) |
| [uuid](uuid.md) | [jvm]<br>open fun [uuid](uuid.md)(pubnub: [PubNub](../../com.pubnub.api.java/-pub-nub/index.md), pnUUIDMetadataResult: [PNUUIDMetadataResult](../../com.pubnub.api.java.models.consumer.objects_api.uuid/-p-n-u-u-i-d-metadata-result/index.md)) |
