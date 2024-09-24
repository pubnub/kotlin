//[pubnub-kotlin-api](../../index.md)/[com.pubnub.api.v2.subscriptions](index.md)

# Package-level declarations

## Types

| Name | Summary |
|---|---|
| [EmptyOptions](-empty-options/index.md) | [common]<br>object [EmptyOptions](-empty-options/index.md) : [SubscriptionOptions](-subscription-options/index.md)<br>A no-op options object. It doesn't modify subscription behavior in any way. |
| [FilterImpl](-filter-impl/index.md) | [common]<br>class [FilterImpl](-filter-impl/index.md) : [SubscriptionOptions](-subscription-options/index.md) |
| [ReceivePresenceEventsImpl](-receive-presence-events-impl/index.md) | [common]<br>object [ReceivePresenceEventsImpl](-receive-presence-events-impl/index.md) : [SubscriptionOptions](-subscription-options/index.md) |
| [SubscribeCapable](-subscribe-capable/index.md) | [common]<br>interface [SubscribeCapable](-subscribe-capable/index.md) |
| [Subscription](-subscription/index.md) | [common]<br>interface [Subscription](-subscription/index.md) : [EventEmitter](../com.pubnub.api.v2.callbacks/-event-emitter/index.md), [SubscribeCapable](-subscribe-capable/index.md), [AutoCloseable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-auto-closeable/index.html)<br>Represents a potential subscription to the PubNub real-time network. |
| [SubscriptionCursor](-subscription-cursor/index.md) | [common]<br>class [SubscriptionCursor](-subscription-cursor/index.md)(val timetoken: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html))<br>A holder for a timetoken value. |
| [SubscriptionOptions](-subscription-options/index.md) | [common]<br>open class [SubscriptionOptions](-subscription-options/index.md)<br>SubscriptionOptions is a mechanism used for supplying optional modifiers for subscriptions. |
| [SubscriptionSet](-subscription-set/index.md) | [common]<br>interface [SubscriptionSet](-subscription-set/index.md) : [EventEmitter](../com.pubnub.api.v2.callbacks/-event-emitter/index.md), [SubscribeCapable](-subscribe-capable/index.md), [AutoCloseable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-auto-closeable/index.html)<br>A helper class that manages multiple [Subscription](-subscription/index.md)s that can be added to it. |
