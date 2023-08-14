//[pubnub-kotlin](../../../index.md)/[com.pubnub.api.callbacks](../index.md)/[SubscribeCallback](index.md)/[status](status.md)

# status

[jvm]\
abstract fun [status](status.md)(pubnub: [PubNub](../../com.pubnub.api/-pub-nub/index.md), pnStatus: [PNStatus](../../com.pubnub.api.models.consumer/-p-n-status/index.md))

Receive status events like [PNStatusCategory.PNAcknowledgmentCategory](../../com.pubnub.api.enums/-p-n-status-category/-p-n-acknowledgment-category/index.md), [PNStatusCategory.PNConnectedCategory](../../com.pubnub.api.enums/-p-n-status-category/-p-n-connected-category/index.md), [PNStatusCategory.PNReconnectedCategory](../../com.pubnub.api.enums/-p-n-status-category/-p-n-reconnected-category/index.md)

and other events related to the subscribe loop and channel mix.

## Parameters

jvm

| | |
|---|---|
| pubnub | The client instance which has this listener attached. |
| pnStatus | API operation metadata. |
