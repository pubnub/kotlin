//[pubnub-core-api](../../../index.md)/[com.pubnub.api](../index.md)/[BasePubNub](index.md)/[reconnect](reconnect.md)

# reconnect

[jvm]\
abstract fun [reconnect](reconnect.md)(timetoken: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html) = 0)

Force the SDK to try and reach out PubNub. Monitor the results in SubscribeCallback.status

#### Parameters

jvm

| | |
|---|---|
| timetoken | optional timetoken to use for the subscriptions on reconnection. Only applicable when PNConfiguration.enableEventEngine is true, otherwise ignored |
