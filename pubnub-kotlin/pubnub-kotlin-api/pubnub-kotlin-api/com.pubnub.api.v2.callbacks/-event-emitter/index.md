//[pubnub-kotlin-api](../../../index.md)/[com.pubnub.api.v2.callbacks](../index.md)/[EventEmitter](index.md)

# EventEmitter

interface [EventEmitter](index.md)

Interface implemented by objects that are the source of real time events from the PubNub network.

#### Inheritors

| |
|---|
| [PubNub](../../com.pubnub.api/-pub-nub/index.md) |
| [Subscription](../../com.pubnub.api.v2.subscriptions/-subscription/index.md) |
| [SubscriptionSet](../../com.pubnub.api.v2.subscriptions/-subscription-set/index.md) |

## Properties

| Name | Summary |
|---|---|
| [onFile](on-file.md) | [common]<br>open var [onFile](on-file.md): ([PNFileEventResult](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.models.consumer.pubsub.files/-p-n-file-event-result/index.md)) -&gt; [Unit](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-unit/index.html)?<br>A nullable property to set a function or lambda for responding to file events. This function is invoked whenever a new file event is received, providing a convenient way to process or react to file events. |
| [onMessage](on-message.md) | [common]<br>open var [onMessage](on-message.md): ([PNMessageResult](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.models.consumer.pubsub/-p-n-message-result/index.md)) -&gt; [Unit](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-unit/index.html)?<br>A nullable property that can be set to a function (or lambda expression) to handle incoming message events. This function is invoked whenever a new message is received, providing a convenient way to process or react to messages. |
| [onMessageAction](on-message-action.md) | [common]<br>open var [onMessageAction](on-message-action.md): ([PNMessageActionResult](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.models.consumer.pubsub.message_actions/-p-n-message-action-result/index.md)) -&gt; [Unit](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-unit/index.html)?<br>A nullable property that allows setting a function or lambda to react to message action events. This function is invoked whenever a new message action is received, providing a convenient way to process or react to message actions. |
| [onObjects](on-objects.md) | [common]<br>open var [onObjects](on-objects.md): ([PNObjectEventResult](../../com.pubnub.api.models.consumer.pubsub.objects/-p-n-object-event-result/index.md)) -&gt; [Unit](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-unit/index.html)?<br>A nullable property for assigning a function or lambda to handle object events. This function is triggered with each new object event, providing a mechanism to manage object-related updates. |
| [onPresence](on-presence.md) | [common]<br>open var [onPresence](on-presence.md): ([PNPresenceEventResult](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.models.consumer.pubsub/-p-n-presence-event-result/index.md)) -&gt; [Unit](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-unit/index.html)?<br>A nullable property designed to set a function or lambda expression for handling incoming presence events. This function is called whenever a new presence event occurs, offering an efficient method for tracking presence updates. |
| [onSignal](on-signal.md) | [common]<br>open var [onSignal](on-signal.md): ([PNSignalResult](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.models.consumer.pubsub/-p-n-signal-result/index.md)) -&gt; [Unit](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-unit/index.html)?<br>A nullable property for assigning a function or lambda expression to handle incoming signal events. This function is called whenever a new signal is received, providing a convenient way to process or react to signals. |

## Functions

| Name | Summary |
|---|---|
| [addListener](add-listener.md) | [common]<br>abstract fun [addListener](add-listener.md)(listener: [EventListener](../-event-listener/index.md))<br>Add a listener. |
| [removeAllListeners](remove-all-listeners.md) | [common]<br>abstract fun [removeAllListeners](remove-all-listeners.md)()<br>Removes all listeners. |
| [removeListener](remove-listener.md) | [common]<br>abstract fun [removeListener](remove-listener.md)(listener: [Listener](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.callbacks/-listener/index.md))<br>Remove a listener. |
