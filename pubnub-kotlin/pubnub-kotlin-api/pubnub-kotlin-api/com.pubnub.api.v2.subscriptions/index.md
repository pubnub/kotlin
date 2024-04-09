//[pubnub-kotlin-api](../../index.md)/[com.pubnub.api.v2.subscriptions](index.md)

# Package-level declarations

## Types

| Name | Summary |
|---|---|
| [Subscription](-subscription/index.md) | [jvm]<br>interface [Subscription](-subscription/index.md) : [BaseSubscription](../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.v2.subscriptions/-base-subscription/index.md)&lt;[EventListener](../com.pubnub.api.v2.callbacks/-event-listener/index.md)&gt; , [EventEmitter](../com.pubnub.api.v2.callbacks/-event-emitter/index.md)<br>Represents a potential subscription to the PubNub real-time network. |
| [SubscriptionSet](-subscription-set/index.md) | [jvm]<br>interface [SubscriptionSet](-subscription-set/index.md) : [BaseSubscriptionSet](../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.v2.subscriptions/-base-subscription-set/index.md)&lt;[EventListener](../com.pubnub.api.v2.callbacks/-event-listener/index.md), [Subscription](-subscription/index.md)&gt; , [EventEmitter](../com.pubnub.api.v2.callbacks/-event-emitter/index.md)<br>A helper class that manages multiple [Subscription](-subscription/index.md)s that can be added to it. |
