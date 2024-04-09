//[pubnub-kotlin-api](../../../index.md)/[com.pubnub.api.v2.callbacks](../index.md)/[EventEmitter](index.md)

# EventEmitter

interface [EventEmitter](index.md) : [BaseEventEmitter](../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.v2.callbacks/-base-event-emitter/index.md)&lt;[EventListener](../-event-listener/index.md)&gt; 

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
| [onFile](on-file.md) | [jvm]<br>abstract var [onFile](on-file.md): ([PNFileEventResult](../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.models.consumer.pubsub.files/-p-n-file-event-result/index.md)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)?<br>A nullable property to set a function or lambda for responding to file events. This function is invoked whenever a new file event is received, providing a convenient way to process or react to file events. |
| [onMessage](on-message.md) | [jvm]<br>abstract var [onMessage](on-message.md): ([PNMessageResult](../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.models.consumer.pubsub/-p-n-message-result/index.md)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)?<br>A nullable property that can be set to a function (or lambda expression) to handle incoming message events. This function is invoked whenever a new message is received, providing a convenient way to process or react to messages. |
| [onMessageAction](on-message-action.md) | [jvm]<br>abstract var [onMessageAction](on-message-action.md): ([PNMessageActionResult](../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.models.consumer.pubsub.message_actions/-p-n-message-action-result/index.md)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)?<br>A nullable property that allows setting a function or lambda to react to message action events. This function is invoked whenever a new message action is received, providing a convenient way to process or react to message actions. |
| [onObjects](on-objects.md) | [jvm]<br>abstract var [onObjects](on-objects.md): ([PNObjectEventResult](../../com.pubnub.api.models.consumer.pubsub.objects/-p-n-object-event-result/index.md)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)?<br>A nullable property for assigning a function or lambda to handle object events. This function is triggered with each new object event, providing a mechanism to manage object-related updates. |
| [onPresence](on-presence.md) | [jvm]<br>abstract var [onPresence](on-presence.md): ([PNPresenceEventResult](../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.models.consumer.pubsub/-p-n-presence-event-result/index.md)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)?<br>A nullable property designed to set a function or lambda expression for handling incoming presence events. This function is called whenever a new presence event occurs, offering an efficient method for tracking presence updates. |
| [onSignal](on-signal.md) | [jvm]<br>abstract var [onSignal](on-signal.md): ([PNSignalResult](../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.models.consumer.pubsub/-p-n-signal-result/index.md)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)?<br>A nullable property for assigning a function or lambda expression to handle incoming signal events. This function is called whenever a new signal is received, providing a convenient way to process or react to signals. |

## Functions

| Name | Summary |
|---|---|
| [addListener](../../com.pubnub.api.v2.subscriptions/-subscription-set/index.md#1732058745%2FFunctions%2F1262999440) | [jvm]<br>abstract fun [addListener](../../com.pubnub.api.v2.subscriptions/-subscription-set/index.md#1732058745%2FFunctions%2F1262999440)(listener: [EventListener](../-event-listener/index.md)) |
| [removeAllListeners](../../com.pubnub.api.v2.subscriptions/-subscription-set/index.md#983921133%2FFunctions%2F1262999440) | [jvm]<br>abstract fun [removeAllListeners](../../com.pubnub.api.v2.subscriptions/-subscription-set/index.md#983921133%2FFunctions%2F1262999440)() |
| [removeListener](../../com.pubnub.api.v2.subscriptions/-subscription-set/index.md#-1323362624%2FFunctions%2F1262999440) | [jvm]<br>abstract fun [removeListener](../../com.pubnub.api.v2.subscriptions/-subscription-set/index.md#-1323362624%2FFunctions%2F1262999440)(listener: [Listener](../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.callbacks/-listener/index.md)) |
