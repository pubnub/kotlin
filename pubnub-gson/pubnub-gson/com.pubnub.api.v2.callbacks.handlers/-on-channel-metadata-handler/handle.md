//[pubnub-gson](../../../index.md)/[com.pubnub.api.v2.callbacks.handlers](../index.md)/[OnChannelMetadataHandler](index.md)/[handle](handle.md)

# handle

[jvm]\
abstract fun [handle](handle.md)(pnChannelMetadataResult: [PNChannelMetadataResult](../../com.pubnub.api.models.consumer.objects_api.channel/-p-n-channel-metadata-result/index.md))

 This interface is designed for implementing custom handlers that respond to channelMetadata event retrieval operations. It defines a single `handle` method that is called with a [PNChannelMetadataResult](../../com.pubnub.api.models.consumer.objects_api.channel/-p-n-channel-metadata-result/index.md) instance, which contains the channel metadata. 

 Usage example: 

```kotlin

OnChannelMetadataHandler handler = pnChannelMetadataResult -> {
    System.out.println("Received channel metadata event: " + pnChannelMetadataResult.getEvent());
};

```

#### See also

| | |
|---|---|
| [PNChannelMetadataResult](../../com.pubnub.api.models.consumer.objects_api.channel/-p-n-channel-metadata-result/index.md) | for more information about the channel metadata result provided to this handler. |
