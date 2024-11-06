//[pubnub-gson-api](../../../../index.md)/[com.pubnub.api.java.callbacks](../../index.md)/[SubscribeCallback](../index.md)/[BaseSubscribeCallback](index.md)/[status](status.md)

# status

[jvm]\
open override fun [status](status.md)(pubnub: [PubNub](../../../com.pubnub.api.java/-pub-nub/index.md), pnStatus: [PNStatus](../../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.models.consumer/-p-n-status/index.md))

Receive status updates from the PubNub client, such as:

- 
   [com.pubnub.api.enums.PNStatusCategory.PNConnectedCategory](../../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.enums/-p-n-status-category/-p-n-connected-category/index.md),
- 
   [com.pubnub.api.enums.PNStatusCategory.PNDisconnectedCategory](../../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.enums/-p-n-status-category/-p-n-disconnected-category/index.md),
- 
   [com.pubnub.api.enums.PNStatusCategory.PNSubscriptionChanged](../../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.enums/-p-n-status-category/-p-n-subscription-changed/index.md)
- 
   [com.pubnub.api.enums.PNStatusCategory.PNConnectionError](../../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.enums/-p-n-status-category/-p-n-connection-error/index.md),
- 
   [com.pubnub.api.enums.PNStatusCategory.PNUnexpectedDisconnectCategory](../../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.enums/-p-n-status-category/-p-n-unexpected-disconnect-category/index.md),

#### Parameters

jvm

| | |
|---|---|
| pubnub | The client instance which has this listener attached. |
| status | Wrapper around the actual message content. |

#### See also

| |
|---|
| [PNStatus](../../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.models.consumer/-p-n-status/index.md) |
