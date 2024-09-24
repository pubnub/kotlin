//[pubnub-kotlin-api](../../../index.md)/[com.pubnub.api.v2.subscriptions](../index.md)/[SubscriptionSet](index.md)

# SubscriptionSet

[common]\
interface [SubscriptionSet](index.md) : [EventEmitter](../../com.pubnub.api.v2.callbacks/-event-emitter/index.md), [SubscribeCapable](../-subscribe-capable/index.md), [AutoCloseable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-auto-closeable/index.html)

A helper class that manages multiple [Subscription](../-subscription/index.md)s that can be added to it.

Use the [com.pubnub.api.PubNub.subscriptionSetOf](../../com.pubnub.api/-pub-nub/subscription-set-of.md) factory methods to create instances of this interface.

Adding multiple `Subscription`s to the set, then calling [subscribe](../../../../../pubnub-kotlin/pubnub-kotlin-api/com.pubnub.api.v2.subscriptions/-subscription-set/subscribe.md) or [unsubscribe](../../../../../pubnub-kotlin/pubnub-kotlin-api/com.pubnub.api.v2.subscriptions/-subscription-set/unsubscribe.md) on the set is more efficient than calling [Subscription.subscribe](../../../../../pubnub-kotlin/pubnub-kotlin-api/com.pubnub.api.v2.subscriptions/-subscription/subscribe.md) on each `Subscription` object separately, as the PubNub client can minimize the number of required reconnections internally.

Remember to always [close](../../../../../pubnub-kotlin/pubnub-kotlin-api/com.pubnub.api.v2.subscriptions/-subscription-set/close.md) the set when you're done using it to avoid memory leaks. Closing the set also closes all `Subscription`s that are part of this set.

## Properties

| Name | Summary |
|---|---|
| [onFile](../../com.pubnub.api.v2.callbacks/-event-emitter/on-file.md) | [common]<br>open var [onFile](../../com.pubnub.api.v2.callbacks/-event-emitter/on-file.md): ([PNFileEventResult](../../com.pubnub.api.models.consumer.pubsub.files/-p-n-file-event-result/index.md)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)?<br>A nullable property to set a function or lambda for responding to file events. This function is invoked whenever a new file event is received, providing a convenient way to process or react to file events. |
| [onMessage](../../com.pubnub.api.v2.callbacks/-event-emitter/on-message.md) | [common]<br>open var [onMessage](../../com.pubnub.api.v2.callbacks/-event-emitter/on-message.md): ([PNMessageResult](../../com.pubnub.api.models.consumer.pubsub/-p-n-message-result/index.md)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)?<br>A nullable property that can be set to a function (or lambda expression) to handle incoming message events. This function is invoked whenever a new message is received, providing a convenient way to process or react to messages. |
| [onMessageAction](../../com.pubnub.api.v2.callbacks/-event-emitter/on-message-action.md) | [common]<br>open var [onMessageAction](../../com.pubnub.api.v2.callbacks/-event-emitter/on-message-action.md): ([PNMessageActionResult](../../com.pubnub.api.models.consumer.pubsub.message_actions/-p-n-message-action-result/index.md)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)?<br>A nullable property that allows setting a function or lambda to react to message action events. This function is invoked whenever a new message action is received, providing a convenient way to process or react to message actions. |
| [onObjects](../../com.pubnub.api.v2.callbacks/-event-emitter/on-objects.md) | [common]<br>open var [onObjects](../../com.pubnub.api.v2.callbacks/-event-emitter/on-objects.md): ([PNObjectEventResult](../../com.pubnub.api.models.consumer.pubsub.objects/-p-n-object-event-result/index.md)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)?<br>A nullable property for assigning a function or lambda to handle object events. This function is triggered with each new object event, providing a mechanism to manage object-related updates. |
| [onPresence](../../com.pubnub.api.v2.callbacks/-event-emitter/on-presence.md) | [common]<br>open var [onPresence](../../com.pubnub.api.v2.callbacks/-event-emitter/on-presence.md): ([PNPresenceEventResult](../../com.pubnub.api.models.consumer.pubsub/-p-n-presence-event-result/index.md)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)?<br>A nullable property designed to set a function or lambda expression for handling incoming presence events. This function is called whenever a new presence event occurs, offering an efficient method for tracking presence updates. |
| [onSignal](../../com.pubnub.api.v2.callbacks/-event-emitter/on-signal.md) | [common]<br>open var [onSignal](../../com.pubnub.api.v2.callbacks/-event-emitter/on-signal.md): ([PNSignalResult](../../com.pubnub.api.models.consumer.pubsub/-p-n-signal-result/index.md)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)?<br>A nullable property for assigning a function or lambda expression to handle incoming signal events. This function is called whenever a new signal is received, providing a convenient way to process or react to signals. |
| [subscriptions](subscriptions.md) | [common]<br>abstract val [subscriptions](subscriptions.md): [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[Subscription](../-subscription/index.md)&gt;<br>Returns an immutable copy of the set of subscriptions contained in this [SubscriptionSet](index.md). |

## Functions

| Name | Summary |
|---|---|
| [add](add.md) | [common]<br>abstract fun [add](add.md)(subscription: [Subscription](../-subscription/index.md))<br>Add a [Subscription](../-subscription/index.md) to this set. |
| [addListener](../../com.pubnub.api.v2.callbacks/-event-emitter/add-listener.md) | [common]<br>abstract fun [addListener](../../com.pubnub.api.v2.callbacks/-event-emitter/add-listener.md)(listener: [EventListener](../../com.pubnub.api.v2.callbacks/-event-listener/index.md))<br>Add a listener. |
| [close](index.md#-2024188833%2FFunctions%2F-1863117221) | [common]<br>expect abstract fun [close](index.md#-2024188833%2FFunctions%2F-1863117221)() |
| [minusAssign](minus-assign.md) | [common]<br>abstract operator fun [minusAssign](minus-assign.md)(subscription: [Subscription](../-subscription/index.md))<br>Remove the [subscription](minus-assign.md) from this set. Equivalent to calling [remove](remove.md). |
| [plusAssign](plus-assign.md) | [common]<br>abstract operator fun [plusAssign](plus-assign.md)(subscription: [Subscription](../-subscription/index.md))<br>Add the [subscription](plus-assign.md) to this SubscriptionSet. Equivalent to calling [add](add.md). |
| [remove](remove.md) | [common]<br>abstract fun [remove](remove.md)(subscription: [Subscription](../-subscription/index.md))<br>Remove the [subscription](remove.md) from this set. |
| [removeAllListeners](../../com.pubnub.api.v2.callbacks/-event-emitter/remove-all-listeners.md) | [common]<br>abstract fun [removeAllListeners](../../com.pubnub.api.v2.callbacks/-event-emitter/remove-all-listeners.md)()<br>Removes all listeners. |
| [removeListener](../../com.pubnub.api.v2.callbacks/-event-emitter/remove-listener.md) | [common]<br>abstract fun [removeListener](../../com.pubnub.api.v2.callbacks/-event-emitter/remove-listener.md)(listener: [Listener](../../com.pubnub.api.callbacks/-listener/index.md))<br>Remove a listener. |
| [subscribe](../-subscribe-capable/subscribe.md) | [common]<br>abstract fun [subscribe](../-subscribe-capable/subscribe.md)(cursor: [SubscriptionCursor](../-subscription-cursor/index.md) = SubscriptionCursor(0))<br>Start receiving events from the subscription (or subscriptions) represented by this object. |
| [unsubscribe](../-subscribe-capable/unsubscribe.md) | [common]<br>abstract fun [unsubscribe](../-subscribe-capable/unsubscribe.md)()<br>Stop receiving events from this subscription. |
