//[pubnub-core-api](../../../index.md)/[com.pubnub.api.v2.subscriptions](../index.md)/[BaseSubscriptionSet](index.md)

# BaseSubscriptionSet

[jvm]\
interface [BaseSubscriptionSet](index.md)&lt;[EvLis](index.md) : [BaseEventListener](../../com.pubnub.api.v2.callbacks/-base-event-listener/index.md), [Subscription](index.md) : [BaseSubscription](../-base-subscription/index.md)&lt;[EvLis](index.md)&gt;&gt; : [BaseEventEmitter](../../com.pubnub.api.v2.callbacks/-base-event-emitter/index.md)&lt;[EvLis](index.md)&gt; , [SubscribeCapable](../-subscribe-capable/index.md), [AutoCloseable](https://docs.oracle.com/javase/8/docs/api/java/lang/AutoCloseable.html)

## Properties

| Name | Summary |
|---|---|
| [subscriptions](subscriptions.md) | [jvm]<br>abstract val [subscriptions](subscriptions.md): [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[Subscription](index.md)&gt;<br>Returns an immutable copy of the set of subscriptions contained in this [BaseSubscriptionSet](index.md). |

## Functions

| Name | Summary |
|---|---|
| [add](add.md) | [jvm]<br>abstract fun [add](add.md)(subscription: [Subscription](index.md))<br>Add a [Subscription](index.md) to this set. |
| [addListener](index.md#722631014%2FFunctions%2F1454713420) | [jvm]<br>abstract fun [addListener](index.md#722631014%2FFunctions%2F1454713420)(listener: [EvLis](index.md))<br>Add a listener. |
| [close](index.md#-1117130810%2FFunctions%2F1454713420) | [jvm]<br>abstract fun [close](index.md#-1117130810%2FFunctions%2F1454713420)() |
| [remove](remove.md) | [jvm]<br>abstract fun [remove](remove.md)(subscription: [Subscription](index.md))<br>Remove the [subscription](remove.md) from this set. |
| [removeAllListeners](../../com.pubnub.api.v2.callbacks/-base-event-emitter/remove-all-listeners.md) | [jvm]<br>abstract fun [removeAllListeners](../../com.pubnub.api.v2.callbacks/-base-event-emitter/remove-all-listeners.md)()<br>Removes all listeners. |
| [removeListener](../../com.pubnub.api.v2.callbacks/-base-event-emitter/remove-listener.md) | [jvm]<br>abstract fun [removeListener](../../com.pubnub.api.v2.callbacks/-base-event-emitter/remove-listener.md)(listener: [Listener](../../com.pubnub.api.callbacks/-listener/index.md))<br>Remove a listener. |
| [subscribe](../-subscribe-capable/subscribe.md) | [jvm]<br>abstract fun [subscribe](../-subscribe-capable/subscribe.md)(cursor: [SubscriptionCursor](../-subscription-cursor/index.md) = SubscriptionCursor(0))<br>Start receiving events from the subscription (or subscriptions) represented by this object. |
| [unsubscribe](../-subscribe-capable/unsubscribe.md) | [jvm]<br>abstract fun [unsubscribe](../-subscribe-capable/unsubscribe.md)()<br>Stop receiving events from this subscription. |
