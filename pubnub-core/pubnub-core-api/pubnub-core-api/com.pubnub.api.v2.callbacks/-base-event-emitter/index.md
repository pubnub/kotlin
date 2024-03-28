//[pubnub-core-api](../../../index.md)/[com.pubnub.api.v2.callbacks](../index.md)/[BaseEventEmitter](index.md)

# BaseEventEmitter

interface [BaseEventEmitter](index.md)&lt;[T](index.md) : [Listener](../../com.pubnub.api.callbacks/-listener/index.md)&gt;

#### Inheritors

| |
|---|
| [BasePubNub](../../com.pubnub.api/-base-pub-nub/index.md) |
| [BaseSubscription](../../com.pubnub.api.v2.subscriptions/-base-subscription/index.md) |
| [BaseSubscriptionSet](../../com.pubnub.api.v2.subscriptions/-base-subscription-set/index.md) |

## Functions

| Name | Summary |
|---|---|
| [addListener](add-listener.md) | [jvm]<br>abstract fun [addListener](add-listener.md)(listener: [T](index.md))<br>Add a listener. |
| [removeAllListeners](remove-all-listeners.md) | [jvm]<br>abstract fun [removeAllListeners](remove-all-listeners.md)()<br>Removes all listeners. |
| [removeListener](remove-listener.md) | [jvm]<br>abstract fun [removeListener](remove-listener.md)(listener: [Listener](../../com.pubnub.api.callbacks/-listener/index.md))<br>Remove a listener. |
