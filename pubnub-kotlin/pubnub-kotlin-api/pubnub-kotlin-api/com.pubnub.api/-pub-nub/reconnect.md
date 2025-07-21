//[pubnub-kotlin-api](../../../index.md)/[com.pubnub.api](../index.md)/[PubNub](index.md)/[reconnect](reconnect.md)

# reconnect

[common]\
expect abstract fun [reconnect](reconnect.md)(timetoken: [Long](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-long/index.html) = 0)actual abstract fun [reconnect](reconnect.md)(timetoken: [Long](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-long/index.html))

[jvm]\
actual abstract fun [reconnect](reconnect.md)(timetoken: [Long](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-long/index.html))

Force the SDK to try and reach out PubNub. Monitor the results in SubscribeCallback.status

#### Parameters

jvm

| | |
|---|---|
| timetoken | optional timetoken to use for the subscriptions on reconnection. |
