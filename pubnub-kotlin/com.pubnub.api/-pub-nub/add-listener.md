//[pubnub-kotlin](../../../index.md)/[com.pubnub.api](../index.md)/[PubNub](index.md)/[addListener](add-listener.md)

# addListener

[jvm]\
fun [addListener](add-listener.md)(listener: [SubscribeCallback](../../com.pubnub.api.callbacks/-subscribe-callback/index.md))

Add a legacy listener for both client status and events. Use fun addListener(listener: EventListener) and fun addListener(listener: StatusListener) instead if possible.

## Parameters

jvm

| | |
|---|---|
| listener | The listener to be added. |

[jvm]\
open override fun [addListener](add-listener.md)(listener: [StatusListener](../../com.pubnub.api.v2.callbacks/-status-listener/index.md))

Add a listener for client status changes.

## Parameters

jvm

| | |
|---|---|
| listener | The listener to be added. |

[jvm]\
open override fun [addListener](add-listener.md)(listener: [EventListener](../../com.pubnub.api.v2.callbacks/-event-listener/index.md))

Add a global listener for events in all active subscriptions.

## Parameters

jvm

| | |
|---|---|
| listener | The listener to be added. |
