//[pubnub-kotlin-api](../../../index.md)/[com.pubnub.api](../index.md)/[PubNub](index.md)/[addListener](add-listener.md)

# addListener

[jvm]\
abstract fun [addListener](add-listener.md)(listener: [SubscribeCallback](../../com.pubnub.api.callbacks/-subscribe-callback/index.md))

Add a legacy listener for both client status and events. Prefer `addListener(EventListener)` and `addListener(StatusListener)` if possible.

#### Parameters

jvm

| | |
|---|---|
| listener | The listener to be added. |

[common, native]\
[common]\
expect abstract fun [addListener](add-listener.md)(listener: [EventListener](../../com.pubnub.api.v2.callbacks/-event-listener/index.md))

[native]\
actual abstract fun [addListener](add-listener.md)(listener: [EventListener](../../com.pubnub.api.v2.callbacks/-event-listener/index.md))

[common]\
expect abstract fun [addListener](add-listener.md)(listener: [StatusListener](../../com.pubnub.api.v2.callbacks/-status-listener/index.md))

[native]\
actual abstract fun [addListener](add-listener.md)(listener: [StatusListener](../../com.pubnub.api.v2.callbacks/-status-listener/index.md))
