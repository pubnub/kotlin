//[pubnub-gson-api](../../../index.md)/[com.pubnub.api.v2.callbacks](../index.md)/[StatusListener](index.md)

# StatusListener

interface [StatusListener](index.md) : [BaseStatusListener](../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.v2.callbacks/-base-status-listener/index.md)

Implement this interface and pass it into [com.pubnub.api.v2.callbacks.StatusEmitter.addListener] to listen for PubNub connection status changes.

#### Inheritors

| |
|---|
| [SubscribeCallback](../../com.pubnub.api.callbacks/-subscribe-callback/index.md) |

## Functions

| Name | Summary |
|---|---|
| [status](status.md) | [jvm]<br>abstract fun [status](status.md)(@NotNullpubnub: @NotNull[PubNub](../../com.pubnub.api/-pub-nub/index.md), @NotNullpnStatus: @NotNull[PNStatus](../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.models.consumer/-p-n-status/index.md)) |
