//[pubnub-kotlin](../../../index.md)/[com.pubnub.api.v2.subscriptions](../index.md)/[Subscription](index.md)

# Subscription

[jvm]\
abstract class [Subscription](index.md) : [EventEmitter](../../com.pubnub.api.v2.callbacks/-event-emitter/index.md), [SubscribeCapable](../-subscribe-capable/index.md), [AutoCloseable](https://docs.oracle.com/javase/8/docs/api/java/lang/AutoCloseable.html)

Represents a potential subscription to [com.pubnub.api.PubNub](../../com.pubnub.api/-pub-nub/index.md).

Create objects of this class through the [com.pubnub.api.v2.entities.Subscribable.subscription](../../com.pubnub.api.v2.entities/-subscribable/subscription.md) method of the respective entities, such as [com.pubnub.api.v2.entities.Channel](../../com.pubnub.api.v2.entities/-channel/index.md), [com.pubnub.api.v2.entities.ChannelGroup](../../com.pubnub.api.v2.entities/-channel-group/index.md), [com.pubnub.api.v2.entities.ChannelMetadata](../../com.pubnub.api.v2.entities/-channel-metadata/index.md) and [com.pubnub.api.v2.entities.UserMetadata](../../com.pubnub.api.v2.entities/-user-metadata/index.md).

Created subscriptions are initially inactive, which means you must call subscribe to start receiving events.

This class implements the [AutoCloseable](https://docs.oracle.com/javase/8/docs/api/java/lang/AutoCloseable.html) interface to help you release resources by calling unsubscribe and removing all listeners on close. Remember to always call close when you no longer need this Subscription.

## Functions

| Name | Summary |
|---|---|
| [addListener](../../com.pubnub.api.v2.callbacks/-event-emitter/add-listener.md) | [jvm]<br>abstract fun [addListener](../../com.pubnub.api.v2.callbacks/-event-emitter/add-listener.md)(listener: [EventListener](../../com.pubnub.api.v2.callbacks/-event-listener/index.md))<br>Add a listener. |
| [close](../-subscription-set/index.md#-1117130810%2FFunctions%2F-1216412040) | [jvm]<br>abstract fun [close](../-subscription-set/index.md#-1117130810%2FFunctions%2F-1216412040)() |
| [plus](plus.md) | [jvm]<br>abstract operator fun [plus](plus.md)(subscription: [Subscription](index.md)): [SubscriptionSet](../-subscription-set/index.md)<br>Create a [SubscriptionSet](../-subscription-set/index.md) that contains both subscriptions. |
| [removeAllListeners](../../com.pubnub.api.v2.callbacks/-event-emitter/remove-all-listeners.md) | [jvm]<br>abstract fun [removeAllListeners](../../com.pubnub.api.v2.callbacks/-event-emitter/remove-all-listeners.md)()<br>Removes all listeners. |
| [removeListener](../../com.pubnub.api.v2.callbacks/-event-emitter/remove-listener.md) | [jvm]<br>abstract fun [removeListener](../../com.pubnub.api.v2.callbacks/-event-emitter/remove-listener.md)(listener: [Listener](../../com.pubnub.api.callbacks/-listener/index.md))<br>Remove a listener. |
| [subscribe](../-subscribe-capable/subscribe.md) | [jvm]<br>abstract fun [subscribe](../-subscribe-capable/subscribe.md)(cursor: [SubscriptionCursor](../-subscription-cursor/index.md) = SubscriptionCursor(0))<br>Start receiving events from the subscription (or subscriptions) represented by this object. |
| [unsubscribe](../-subscribe-capable/unsubscribe.md) | [jvm]<br>abstract fun [unsubscribe](../-subscribe-capable/unsubscribe.md)()<br>Stop receiving events from this subscription. |

## Properties

| Name | Summary |
|---|---|
| [onFile](../../com.pubnub.api.v2.callbacks/-event-emitter/on-file.md) | [jvm]<br>abstract var [onFile](../../com.pubnub.api.v2.callbacks/-event-emitter/on-file.md): ([PNFileEventResult](../../com.pubnub.api.models.consumer.pubsub.files/-p-n-file-event-result/index.md)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)?<br>A nullable property to set a function or lambda for responding to file events. This function is invoked whenever a new file event is received, providing a convenient way to process or react to file events. |
| [onMessage](../../com.pubnub.api.v2.callbacks/-event-emitter/on-message.md) | [jvm]<br>abstract var [onMessage](../../com.pubnub.api.v2.callbacks/-event-emitter/on-message.md): ([PNMessageResult](../../com.pubnub.api.models.consumer.pubsub/-p-n-message-result/index.md)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)?<br>A nullable property that can be set to a function (or lambda expression) to handle incoming message events. This function is invoked whenever a new message is received, providing a convenient way to process or react to messages. |
| [onMessageAction](../../com.pubnub.api.v2.callbacks/-event-emitter/on-message-action.md) | [jvm]<br>abstract var [onMessageAction](../../com.pubnub.api.v2.callbacks/-event-emitter/on-message-action.md): ([PNMessageActionResult](../../com.pubnub.api.models.consumer.pubsub.message_actions/-p-n-message-action-result/index.md)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)?<br>A nullable property that allows setting a function or lambda to react to message action events. This function is invoked whenever a new message action is received, providing a convenient way to process or react to message actions. |
| [onObjects](../../com.pubnub.api.v2.callbacks/-event-emitter/on-objects.md) | [jvm]<br>abstract var [onObjects](../../com.pubnub.api.v2.callbacks/-event-emitter/on-objects.md): ([PNObjectEventResult](../../com.pubnub.api.models.consumer.pubsub.objects/-p-n-object-event-result/index.md)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)?<br>A nullable property for assigning a function or lambda to handle object events. This function is triggered with each new object event, providing a mechanism to manage object-related updates. |
| [onPresence](../../com.pubnub.api.v2.callbacks/-event-emitter/on-presence.md) | [jvm]<br>abstract var [onPresence](../../com.pubnub.api.v2.callbacks/-event-emitter/on-presence.md): ([PNPresenceEventResult](../../com.pubnub.api.models.consumer.pubsub/-p-n-presence-event-result/index.md)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)?<br>A nullable property designed to set a function or lambda expression for handling incoming presence events. This function is called whenever a new presence event occurs, offering an efficient method for tracking presence updates. |
| [onSignal](../../com.pubnub.api.v2.callbacks/-event-emitter/on-signal.md) | [jvm]<br>abstract var [onSignal](../../com.pubnub.api.v2.callbacks/-event-emitter/on-signal.md): ([PNSignalResult](../../com.pubnub.api.models.consumer.pubsub/-p-n-signal-result/index.md)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)?<br>A nullable property for assigning a function or lambda expression to handle incoming signal events. This function is called whenever a new signal is received, providing a convenient way to process or react to signals. |
