//[pubnub-gson-api](../../../index.md)/[com.pubnub.api.v2.callbacks](../index.md)/[EventEmitter](index.md)

# EventEmitter

interface [EventEmitter](index.md) : [BaseEventEmitter](../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.v2.callbacks/-base-event-emitter/index.md)&lt;[T](../../../../../pubnub-core/pubnub-core-api/com.pubnub.api.v2.callbacks/-base-event-emitter/index.md)&gt; 

Interface implemented by objects that are the source of real time events from the PubNub network.

#### Inheritors

| |
|---|
| [Subscription](../../com.pubnub.api.v2.subscriptions/-subscription/index.md) |
| [SubscriptionSet](../../com.pubnub.api.v2.subscriptions/-subscription-set/index.md) |

## Functions

| Name | Summary |
|---|---|
| [addListener](index.md#330403064%2FFunctions%2F126356644) | [jvm]<br>abstract fun [addListener](index.md#330403064%2FFunctions%2F126356644)(listener: [T](../../../../../pubnub-core/pubnub-core-api/com.pubnub.api.v2.callbacks/-base-event-emitter/index.md)) |
| [removeAllListeners](../../com.pubnub.api.v2.subscriptions/-subscription-set/index.md#983921133%2FFunctions%2F126356644) | [jvm]<br>abstract fun [removeAllListeners](../../com.pubnub.api.v2.subscriptions/-subscription-set/index.md#983921133%2FFunctions%2F126356644)() |
| [removeListener](index.md#-1323362624%2FFunctions%2F126356644) | [jvm]<br>abstract fun [removeListener](index.md#-1323362624%2FFunctions%2F126356644)(listener: [Listener](../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.callbacks/-listener/index.md)) |
| [setOnChannelMetadata](set-on-channel-metadata.md) | [jvm]<br>abstract fun [setOnChannelMetadata](set-on-channel-metadata.md)(onChannelMetadataHandler: [OnChannelMetadataHandler](../../com.pubnub.api.v2.callbacks.handlers/-on-channel-metadata-handler/index.md))<br>Sets the handler for incoming channelMetadata events. |
| [setOnFile](set-on-file.md) | [jvm]<br>abstract fun [setOnFile](set-on-file.md)(onFileHandler: [OnFileHandler](../../com.pubnub.api.v2.callbacks.handlers/-on-file-handler/index.md))<br>Sets the handler for incoming file events. |
| [setOnMembership](set-on-membership.md) | [jvm]<br>abstract fun [setOnMembership](set-on-membership.md)(onMembershipHandler: [OnMembershipHandler](../../com.pubnub.api.v2.callbacks.handlers/-on-membership-handler/index.md))<br>Sets the handler for incoming membership events. |
| [setOnMessage](set-on-message.md) | [jvm]<br>abstract fun [setOnMessage](set-on-message.md)(onMessageHandler: [OnMessageHandler](../../com.pubnub.api.v2.callbacks.handlers/-on-message-handler/index.md))<br>Sets the handler for incoming message events. |
| [setOnMessageAction](set-on-message-action.md) | [jvm]<br>abstract fun [setOnMessageAction](set-on-message-action.md)(onMessageActionHandler: [OnMessageActionHandler](../../com.pubnub.api.v2.callbacks.handlers/-on-message-action-handler/index.md))<br>Sets the handler for incoming messageAction events. |
| [setOnPresence](set-on-presence.md) | [jvm]<br>abstract fun [setOnPresence](set-on-presence.md)(onPresenceHandler: [OnPresenceHandler](../../com.pubnub.api.v2.callbacks.handlers/-on-presence-handler/index.md))<br>Sets the handler for incoming presence events. |
| [setOnSignal](set-on-signal.md) | [jvm]<br>abstract fun [setOnSignal](set-on-signal.md)(onSignalHandler: [OnSignalHandler](../../com.pubnub.api.v2.callbacks.handlers/-on-signal-handler/index.md))<br>Sets the handler for incoming signal events. |
| [setOnUuidMetadata](set-on-uuid-metadata.md) | [jvm]<br>abstract fun [setOnUuidMetadata](set-on-uuid-metadata.md)(onUuidMetadataHandler: [OnUuidMetadataHandler](../../com.pubnub.api.v2.callbacks.handlers/-on-uuid-metadata-handler/index.md))<br>Sets the handler for incoming uuidMetadata events. |
