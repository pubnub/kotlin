//[pubnub-kotlin-api](../../../../index.md)/[[root]](../../index.md)/[PubNub](../index.md)/[Push](index.md)

# Push

[js]\
interface [Push](index.md)

## Functions

| Name | Summary |
|---|---|
| [addChannels](add-channels.md) | [js]<br>abstract fun [addChannels](add-channels.md)(params: [PubNub.PushChannelParameters](../-push-channel-parameters/index.md)): [Promise](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.js/-promise/index.html)&lt;[Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)&gt;<br>abstract fun [addChannels](add-channels.md)(params: [PubNub.PushChannelParameters](../-push-channel-parameters/index.md), callback: [StatusCallback](../../-status-callback/index.md)) |
| [deleteDevice](delete-device.md) | [js]<br>abstract fun [deleteDevice](delete-device.md)(params: [PubNub.PushDeviceParameters](../-push-device-parameters/index.md)): [Promise](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.js/-promise/index.html)&lt;[Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)&gt;<br>abstract fun [deleteDevice](delete-device.md)(params: [PubNub.PushDeviceParameters](../-push-device-parameters/index.md), callback: [StatusCallback](../../-status-callback/index.md)) |
| [listChannels](list-channels.md) | [js]<br>abstract fun [listChannels](list-channels.md)(params: [PubNub.PushDeviceParameters](../-push-device-parameters/index.md)): [Promise](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.js/-promise/index.html)&lt;[PubNub.PushListChannelsResponse](../-push-list-channels-response/index.md)&gt;<br>abstract fun [listChannels](list-channels.md)(params: [PubNub.PushDeviceParameters](../-push-device-parameters/index.md), callback: [Callback](../../-callback/index.md)&lt;[PubNub.PushListChannelsResponse](../-push-list-channels-response/index.md)&gt;) |
| [removeChannels](remove-channels.md) | [js]<br>abstract fun [removeChannels](remove-channels.md)(params: [PubNub.PushChannelParameters](../-push-channel-parameters/index.md)): [Promise](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.js/-promise/index.html)&lt;[Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)&gt;<br>abstract fun [removeChannels](remove-channels.md)(params: [PubNub.PushChannelParameters](../-push-channel-parameters/index.md), callback: [StatusCallback](../../-status-callback/index.md)) |
