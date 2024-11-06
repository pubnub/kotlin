//[pubnub-gson-api](../../../index.md)/[com.pubnub.api.java.v2.entities](../index.md)/[Channel](index.md)/[signal](signal.md)

# signal

[jvm]\
abstract fun [signal](signal.md)(message: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)): [Signal](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.endpoints.pubsub/-signal/index.md)

Send a signal to all subscribers of a channel.

By default, signals are limited to a message payload size of 30 bytes. This limit applies only to the payload, and not to the URI or headers. If you require a larger payload size, please [contact support](mailto:support@pubnub.com).

#### Parameters

jvm

| | |
|---|---|
| message | The payload which will be serialized and sent. |
