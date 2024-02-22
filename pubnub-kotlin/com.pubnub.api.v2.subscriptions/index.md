//[pubnub-kotlin](../../index.md)/[com.pubnub.api.v2.subscriptions](index.md)

# Package com.pubnub.api.v2.subscriptions

## Types

| Name | Summary |
|---|---|
| [SubscribeCapable](-subscribe-capable/index.md) | [jvm]<br>interface [SubscribeCapable](-subscribe-capable/index.md) |
| [Subscription](-subscription/index.md) | [jvm]<br>abstract class [Subscription](-subscription/index.md) : [EventEmitter](../com.pubnub.api.v2.callbacks/-event-emitter/index.md), [SubscribeCapable](-subscribe-capable/index.md), [AutoCloseable](https://docs.oracle.com/javase/8/docs/api/java/lang/AutoCloseable.html)<br>Represents a potential subscription to [com.pubnub.api.PubNub](../com.pubnub.api/-pub-nub/index.md). |
| [SubscriptionCursor](-subscription-cursor/index.md) | [jvm]<br>class [SubscriptionCursor](-subscription-cursor/index.md)(val timetoken: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html))<br>A holder for a timetoken value. |
| [SubscriptionOptions](-subscription-options/index.md) | [jvm]<br>open class [SubscriptionOptions](-subscription-options/index.md)<br>SubscriptionOptions is a mechanism used for supplying optional modifiers for subscriptions. |
| [SubscriptionSet](-subscription-set/index.md) | [jvm]<br>abstract class [SubscriptionSet](-subscription-set/index.md) : [EventEmitter](../com.pubnub.api.v2.callbacks/-event-emitter/index.md), [SubscribeCapable](-subscribe-capable/index.md), [AutoCloseable](https://docs.oracle.com/javase/8/docs/api/java/lang/AutoCloseable.html)<br>A helper class that manages multiple [Subscription](-subscription/index.md)s that can be added to it. |
