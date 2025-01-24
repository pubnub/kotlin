//[pubnub-kotlin-api](../../../index.md)/[com.pubnub.api.v2.entities](../index.md)/[Channel](index.md)/[signal](signal.md)

# signal

[common]\
abstract fun [signal](signal.md)(message: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-any/index.html), customMessageType: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)? = null): [Signal](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.endpoints.pubsub/-signal/index.md)

Send a signal to all subscribers of the channel.

By default, signals are limited to a message payload size of 30 bytes. This limit applies only to the payload, and not to the URI or headers. If you require a larger payload size, please [contact support](mailto:support@pubnub.com).

#### Parameters

common

| | |
|---|---|
| message | The payload which will be serialized and sent. |
| customMessageType | The custom type associated with the message. |
