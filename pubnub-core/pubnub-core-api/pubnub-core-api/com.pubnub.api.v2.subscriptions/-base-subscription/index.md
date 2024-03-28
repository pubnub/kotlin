//[pubnub-core-api](../../../index.md)/[com.pubnub.api.v2.subscriptions](../index.md)/[BaseSubscription](index.md)

# BaseSubscription

[jvm]\
interface [BaseSubscription](index.md)&lt;[EvLis](index.md) : [BaseEventListener](../../com.pubnub.api.v2.callbacks/-base-event-listener/index.md)&gt; : [BaseEventEmitter](../../com.pubnub.api.v2.callbacks/-base-event-emitter/index.md)&lt;[EvLis](index.md)&gt; , [SubscribeCapable](../-subscribe-capable/index.md), [AutoCloseable](https://docs.oracle.com/javase/8/docs/api/java/lang/AutoCloseable.html)

Represents a potential subscription to the PubNub real-time network.

Create objects of this class through the [com.pubnub.api.v2.entities.Subscribable.subscription](../../com.pubnub.api.v2.entities/-subscribable/subscription.md) method of the respective entities, such as [com.pubnub.api.v2.entities.BaseChannel](../../com.pubnub.api.v2.entities/-base-channel/index.md), [com.pubnub.api.v2.entities.BaseChannelGroup](../../com.pubnub.api.v2.entities/-base-channel-group/index.md), [com.pubnub.api.v2.entities.BaseChannelMetadata](../../com.pubnub.api.v2.entities/-base-channel-metadata/index.md) and [com.pubnub.api.v2.entities.BaseUserMetadata](../../com.pubnub.api.v2.entities/-base-user-metadata/index.md).

Created subscriptions are initially inactive, which means you must call [subscribe](../../../../../pubnub-gson/com.pubnub.api.v2.subscriptions/-base-subscription/subscribe.md) to start receiving events.

This class implements the [AutoCloseable](https://docs.oracle.com/javase/8/docs/api/java/lang/AutoCloseable.html) interface to help you release resources by calling [unsubscribe](../../../../../pubnub-gson/com.pubnub.api.v2.subscriptions/-base-subscription/unsubscribe.md) and removing all listeners on [close](../../../../../pubnub-gson/com.pubnub.api.v2.subscriptions/-base-subscription/close.md). Remember to always call [close](../../../../../pubnub-gson/com.pubnub.api.v2.subscriptions/-base-subscription/close.md) when you no longer need this Subscription.

## Functions

| Name | Summary |
|---|---|
| [addListener](../-base-subscription-set/index.md#722631014%2FFunctions%2F1454713420) | [jvm]<br>abstract fun [addListener](../-base-subscription-set/index.md#722631014%2FFunctions%2F1454713420)(listener: [EvLis](index.md))<br>Add a listener. |
| [close](../-base-subscription-set/index.md#-1117130810%2FFunctions%2F1454713420) | [jvm]<br>abstract fun [close](../-base-subscription-set/index.md#-1117130810%2FFunctions%2F1454713420)() |
| [removeAllListeners](../../com.pubnub.api.v2.callbacks/-base-event-emitter/remove-all-listeners.md) | [jvm]<br>abstract fun [removeAllListeners](../../com.pubnub.api.v2.callbacks/-base-event-emitter/remove-all-listeners.md)()<br>Removes all listeners. |
| [removeListener](../../com.pubnub.api.v2.callbacks/-base-event-emitter/remove-listener.md) | [jvm]<br>abstract fun [removeListener](../../com.pubnub.api.v2.callbacks/-base-event-emitter/remove-listener.md)(listener: [Listener](../../com.pubnub.api.callbacks/-listener/index.md))<br>Remove a listener. |
| [subscribe](../-subscribe-capable/subscribe.md) | [jvm]<br>abstract fun [subscribe](../-subscribe-capable/subscribe.md)(cursor: [SubscriptionCursor](../-subscription-cursor/index.md) = SubscriptionCursor(0))<br>Start receiving events from the subscription (or subscriptions) represented by this object. |
| [unsubscribe](../-subscribe-capable/unsubscribe.md) | [jvm]<br>abstract fun [unsubscribe](../-subscribe-capable/unsubscribe.md)()<br>Stop receiving events from this subscription. |
