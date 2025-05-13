//[pubnub-kotlin-api](../../../index.md)/[com.pubnub.api](../index.md)/[PubNub](index.md)/[signal](signal.md)

# signal

[common]\
expect abstract fun [signal](signal.md)(channel: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), message: [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html), customMessageType: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)? = null): [Signal](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.endpoints.pubsub/-signal/index.md)actual abstract fun [signal](signal.md)(channel: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), message: [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html), customMessageType: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)?): [Signal](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.endpoints.pubsub/-signal/index.md)

[jvm]\
actual abstract fun [signal](signal.md)(channel: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), message: [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html), customMessageType: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)?): [Signal](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.endpoints.pubsub/-signal/index.md)

Send a signal to all subscribers of a channel.

By default, signals are limited to a message payload size of 30 bytes. This limit applies only to the payload, and not to the URI or headers. If you require a larger payload size, please [contact support](mailto:support@pubnub.com).

#### Parameters

jvm

| | |
|---|---|
| channel | The channel which the signal will be sent to. |
| message | The payload which will be serialized and sent. |
| customMessageType | The custom type associated with the message. |
