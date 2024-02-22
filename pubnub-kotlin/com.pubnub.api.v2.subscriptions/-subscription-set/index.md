//[pubnub-kotlin](../../../index.md)/[com.pubnub.api.v2.subscriptions](../index.md)/[SubscriptionSet](index.md)

# SubscriptionSet

[jvm]\
abstract class [SubscriptionSet](index.md) : [EventEmitter](../../com.pubnub.api.v2.callbacks/-event-emitter/index.md), [SubscribeCapable](../-subscribe-capable/index.md), [AutoCloseable](https://docs.oracle.com/javase/8/docs/api/java/lang/AutoCloseable.html)

A helper class that manages multiple [Subscription](../-subscription/index.md)s that can be added to it.

Use the [com.pubnub.api.PubNub.subscriptionSetOf](../../com.pubnub.api/-pub-nub/subscription-set-of.md) factory methods to create instances of this class.

Adding multiple `Subscription`s to the set, then calling subscribe or unsubscribe on the set is more efficient than calling `subscribe` on all `Subscription` objects separately, as the PubNub client can minimize the number of required reconnections internally.

Remember to always close the set when you're done with it to avoid memory leaks. Closing the set also closes all `Subscription`s that are part of this set.

## Functions

| Name | Summary |
|---|---|
| [add](add.md) | [jvm]<br>abstract fun [add](add.md)(subscription: [Subscription](../-subscription/index.md))<br>Add a [Subscription](../-subscription/index.md) to this set. |
| [addListener](../../com.pubnub.api.v2.callbacks/-event-emitter/add-listener.md) | [jvm]<br>abstract fun [addListener](../../com.pubnub.api.v2.callbacks/-event-emitter/add-listener.md)(listener: [EventListener](../../com.pubnub.api.v2.callbacks/-event-listener/index.md))<br>Add a listener. |
| [close](index.md#-1117130810%2FFunctions%2F-1216412040) | [jvm]<br>abstract fun [close](index.md#-1117130810%2FFunctions%2F-1216412040)() |
| [minus](minus.md) | [jvm]<br>operator fun [minus](minus.md)(subscription: [Subscription](../-subscription/index.md))<br>Remove a [Subscription](../-subscription/index.md) from this set. Equivalent to calling [remove](remove.md). |
| [plus](plus.md) | [jvm]<br>abstract operator fun [plus](plus.md)(subscription: [Subscription](../-subscription/index.md)): [SubscriptionSet](index.md)<br>Adds a [Subscription](../-subscription/index.md) to this set. Equivalent to calling [add](add.md). |
| [remove](remove.md) | [jvm]<br>abstract fun [remove](remove.md)(subscription: [Subscription](../-subscription/index.md))<br>Remove a [Subscription](../-subscription/index.md) from this set. |
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
| [subscriptions](subscriptions.md) | [jvm]<br>abstract val [subscriptions](subscriptions.md): [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[Subscription](../-subscription/index.md)&gt;<br>Returns an immutable copy of the set of subscriptions contained in this [SubscriptionSet](index.md). |
