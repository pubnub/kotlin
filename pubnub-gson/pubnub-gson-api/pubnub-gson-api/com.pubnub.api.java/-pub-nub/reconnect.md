//[pubnub-gson-api](../../../index.md)/[com.pubnub.api.java](../index.md)/[PubNub](index.md)/[reconnect](reconnect.md)

# reconnect

[jvm]\
abstract fun [reconnect](reconnect.md)()

[jvm]\
abstract fun [reconnect](reconnect.md)(timetoken: [Long](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-long/index.html) = 0)

Force the SDK to try and reach out PubNub. Monitor the results in [SubscribeCallback.status](../../../../../pubnub-gson/pubnub-gson-api/com.pubnub.api.java.callbacks/-subscribe-callback/status.md)

#### Parameters

jvm

| | |
|---|---|
| timetoken | optional timetoken to use for the subscriptions on reconnection. |
