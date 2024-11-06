//[pubnub-gson-api](../../../index.md)/[com.pubnub.api.java.v2.callbacks](../index.md)/[EventEmitter](index.md)

# EventEmitter

interface [EventEmitter](index.md)

Interface implemented by objects that are the source of real time events from the PubNub network.

#### Inheritors

| |
|---|
| [PubNub](../../com.pubnub.api.java/-pub-nub/index.md) |
| [Subscription](../../com.pubnub.api.java.v2.subscriptions/-subscription/index.md) |
| [SubscriptionSet](../../com.pubnub.api.java.v2.subscriptions/-subscription-set/index.md) |

## Functions

| Name | Summary |
|---|---|
| [addListener](add-listener.md) | [jvm]<br>abstract fun [addListener](add-listener.md)(listener: [EventListener](../-event-listener/index.md))<br>Add a listener. |
| [removeAllListeners](remove-all-listeners.md) | [jvm]<br>abstract fun [removeAllListeners](remove-all-listeners.md)()<br>Removes all listeners. |
| [removeListener](remove-listener.md) | [jvm]<br>abstract fun [removeListener](remove-listener.md)(listener: [Listener](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.callbacks/-listener/index.md))<br>Remove a listener. |
| [setOnChannelMetadata](set-on-channel-metadata.md) | [jvm]<br>abstract fun [setOnChannelMetadata](set-on-channel-metadata.md)(onChannelMetadataHandler: [OnChannelMetadataHandler](../../com.pubnub.api.java.v2.callbacks.handlers/-on-channel-metadata-handler/index.md)?)<br>Sets the handler for incoming channelMetadata events. This method allows the assignment of an [OnChannelMetadataHandler](../../com.pubnub.api.java.v2.callbacks.handlers/-on-channel-metadata-handler/index.md) implementation or lambda expression to handle incoming presence events. |
| [setOnFile](set-on-file.md) | [jvm]<br>abstract fun [setOnFile](set-on-file.md)(onFileHandler: [OnFileHandler](../../com.pubnub.api.java.v2.callbacks.handlers/-on-file-handler/index.md)?)<br>Sets the handler for incoming file events. This method allows the assignment of an [OnFileHandler](../../com.pubnub.api.java.v2.callbacks.handlers/-on-file-handler/index.md) implementation or lambda expression to handle incoming presence events. |
| [setOnMembership](set-on-membership.md) | [jvm]<br>abstract fun [setOnMembership](set-on-membership.md)(onMembershipHandler: [OnMembershipHandler](../../com.pubnub.api.java.v2.callbacks.handlers/-on-membership-handler/index.md)?)<br>Sets the handler for incoming membership events. This method allows the assignment of an [OnMembershipHandler](../../com.pubnub.api.java.v2.callbacks.handlers/-on-membership-handler/index.md) implementation or lambda expression to handle incoming presence events. |
| [setOnMessage](set-on-message.md) | [jvm]<br>abstract fun [setOnMessage](set-on-message.md)(onMessageHandler: [OnMessageHandler](../../com.pubnub.api.java.v2.callbacks.handlers/-on-message-handler/index.md)?)<br>Sets the handler for incoming message events. This method allows the assignment of an [OnMessageHandler](../../com.pubnub.api.java.v2.callbacks.handlers/-on-message-handler/index.md) implementation or lambda expression to handle incoming messages. |
| [setOnMessageAction](set-on-message-action.md) | [jvm]<br>abstract fun [setOnMessageAction](set-on-message-action.md)(onMessageActionHandler: [OnMessageActionHandler](../../com.pubnub.api.java.v2.callbacks.handlers/-on-message-action-handler/index.md)?)<br>Sets the handler for incoming messageAction events. This method allows the assignment of an [OnMessageActionHandler](../../com.pubnub.api.java.v2.callbacks.handlers/-on-message-action-handler/index.md) implementation or lambda expression to handle incoming presence events. |
| [setOnPresence](set-on-presence.md) | [jvm]<br>abstract fun [setOnPresence](set-on-presence.md)(onPresenceHandler: [OnPresenceHandler](../../com.pubnub.api.java.v2.callbacks.handlers/-on-presence-handler/index.md)?)<br>Sets the handler for incoming presence events. This method allows the assignment of an [OnPresenceHandler](../../com.pubnub.api.java.v2.callbacks.handlers/-on-presence-handler/index.md) implementation or lambda expression to handle incoming presence events. |
| [setOnSignal](set-on-signal.md) | [jvm]<br>abstract fun [setOnSignal](set-on-signal.md)(onSignalHandler: [OnSignalHandler](../../com.pubnub.api.java.v2.callbacks.handlers/-on-signal-handler/index.md)?)<br>Sets the handler for incoming signal events. This method allows the assignment of an [OnSignalHandler](../../com.pubnub.api.java.v2.callbacks.handlers/-on-signal-handler/index.md) implementation or lambda expression to handle incoming signals. |
| [setOnUuidMetadata](set-on-uuid-metadata.md) | [jvm]<br>abstract fun [setOnUuidMetadata](set-on-uuid-metadata.md)(onUuidMetadataHandler: [OnUuidMetadataHandler](../../com.pubnub.api.java.v2.callbacks.handlers/-on-uuid-metadata-handler/index.md)?)<br>Sets the handler for incoming uuidMetadata events. This method allows the assignment of an [OnUuidMetadataHandler](../../com.pubnub.api.java.v2.callbacks.handlers/-on-uuid-metadata-handler/index.md) implementation or lambda expression to handle incoming presence events. |
