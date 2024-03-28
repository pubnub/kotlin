//[pubnub-core-api](../../index.md)/[com.pubnub.api.v2.subscriptions](index.md)

# Package-level declarations

## Types

| Name | Summary |
|---|---|
| [BaseSubscription](-base-subscription/index.md) | [jvm]<br>interface [BaseSubscription](-base-subscription/index.md)&lt;[EvLis](-base-subscription/index.md) : [BaseEventListener](../com.pubnub.api.v2.callbacks/-base-event-listener/index.md)&gt; : [BaseEventEmitter](../com.pubnub.api.v2.callbacks/-base-event-emitter/index.md)&lt;[EvLis](-base-subscription/index.md)&gt; , [SubscribeCapable](-subscribe-capable/index.md), [AutoCloseable](https://docs.oracle.com/javase/8/docs/api/java/lang/AutoCloseable.html)<br>Represents a potential subscription to the PubNub real-time network. |
| [BaseSubscriptionSet](-base-subscription-set/index.md) | [jvm]<br>interface [BaseSubscriptionSet](-base-subscription-set/index.md)&lt;[EvLis](-base-subscription-set/index.md) : [BaseEventListener](../com.pubnub.api.v2.callbacks/-base-event-listener/index.md), [Subscription](-base-subscription-set/index.md) : [BaseSubscription](-base-subscription/index.md)&lt;[EvLis](-base-subscription-set/index.md)&gt;&gt; : [BaseEventEmitter](../com.pubnub.api.v2.callbacks/-base-event-emitter/index.md)&lt;[EvLis](-base-subscription-set/index.md)&gt; , [SubscribeCapable](-subscribe-capable/index.md), [AutoCloseable](https://docs.oracle.com/javase/8/docs/api/java/lang/AutoCloseable.html) |
| [EmptyOptions](-empty-options/index.md) | [jvm]<br>object [EmptyOptions](-empty-options/index.md) : [SubscriptionOptions](-subscription-options/index.md)<br>A no-op options object. It doesn't modify subscription behavior in any way. |
| [FilterImpl](-filter-impl/index.md) | [jvm]<br>class [FilterImpl](-filter-impl/index.md) : [SubscriptionOptions](-subscription-options/index.md) |
| [ReceivePresenceEventsImpl](-receive-presence-events-impl/index.md) | [jvm]<br>object [ReceivePresenceEventsImpl](-receive-presence-events-impl/index.md) : [SubscriptionOptions](-subscription-options/index.md) |
| [SubscribeCapable](-subscribe-capable/index.md) | [jvm]<br>interface [SubscribeCapable](-subscribe-capable/index.md) |
| [SubscriptionCursor](-subscription-cursor/index.md) | [jvm]<br>class [SubscriptionCursor](-subscription-cursor/index.md)(val timetoken: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html))<br>A holder for a timetoken value. |
| [SubscriptionOptions](-subscription-options/index.md) | [jvm]<br>open class [SubscriptionOptions](-subscription-options/index.md)<br>SubscriptionOptions is a mechanism used for supplying optional modifiers for subscriptions. |
