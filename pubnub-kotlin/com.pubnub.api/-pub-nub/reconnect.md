//[pubnub-kotlin](../../../index.md)/[com.pubnub.api](../index.md)/[PubNub](index.md)/[reconnect](reconnect.md)

# reconnect

[jvm]\
fun [reconnect](reconnect.md)(timetoken: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html) = 0)

Force the SDK to try and reach out PubNub. Monitor the results in [SubscribeCallback.status](../../com.pubnub.api.callbacks/-subscribe-callback/status.md)

## Parameters

jvm

| | |
|---|---|
| timetoken | optional timetoken to use for the subscription on reconnection. Only applicable when [PNConfiguration.enableEventEngine](../-p-n-configuration/enable-event-engine.md) is true, otherwise ignored |
