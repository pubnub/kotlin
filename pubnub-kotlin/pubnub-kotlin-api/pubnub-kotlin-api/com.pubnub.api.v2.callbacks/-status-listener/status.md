//[pubnub-kotlin-api](../../../index.md)/[com.pubnub.api.v2.callbacks](../index.md)/[StatusListener](index.md)/[status](status.md)

# status

[jvm]\
abstract fun [status](status.md)(pubnub: [PubNub](../../com.pubnub.api/-pub-nub/index.md), status: [PNStatus](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.models.consumer/-p-n-status/index.md))

Receive status updates from the PubNub client, such as:

- 
   PNStatusCategory.PNConnectedCategory,
- 
   PNStatusCategory.PNDisconnectedCategory,
- 
   PNStatusCategory.PNSubscriptionChanged
- 
   PNStatusCategory.PNConnectionError,
- 
   PNStatusCategory.PNUnexpectedDisconnectCategory,

#### Parameters

jvm

| | |
|---|---|
| pubnub | The client instance which has this listener attached. |
| status | Wrapper around the actual message content. |

#### See also

| |
|---|
| [PNStatus](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.models.consumer/-p-n-status/index.md) |
