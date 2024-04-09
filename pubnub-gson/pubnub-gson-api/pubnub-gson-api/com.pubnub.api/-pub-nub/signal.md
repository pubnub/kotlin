//[pubnub-gson-api](../../../index.md)/[com.pubnub.api](../index.md)/[PubNub](index.md)/[signal](signal.md)

# signal

[jvm]\
abstract fun [signal](signal.md)(message: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html), channel: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [SignalBuilder](../../com.pubnub.api.v2.endpoints.pubsub/-signal-builder/index.md)

Send a signal to all subscribers of a channel.

By default, signals are limited to a message payload size of 30 bytes. This limit applies only to the payload, and not to the URI or headers. If you require a larger payload size, please [contact support](mailto:support@pubnub.com).

[jvm]\
abstract fun [~~signal~~](signal.md)(): [Signal](../../com.pubnub.api.endpoints.pubsub/-signal/index.md)

---

### Deprecated

Use signal(Object, String) instead

#### Replace with

```kotlin
signal(message, channel)
```
---

Send a signal to all subscribers of a channel.

By default, signals are limited to a message payload size of 30 bytes. This limit applies only to the payload, and not to the URI or headers. If you require a larger payload size, please [contact support](mailto:support@pubnub.com).
