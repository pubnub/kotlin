//[pubnub-gson](../../../index.md)/[com.pubnub.api.v2.callbacks.handlers](../index.md)/[OnUuidMetadataHandler](index.md)/[handle](handle.md)

# handle

[jvm]\
abstract fun [handle](handle.md)(pnUUIDMetadataResult: [PNUUIDMetadataResult](../../com.pubnub.api.models.consumer.objects_api.uuid/-p-n-u-u-i-d-metadata-result/index.md))

 This interface is designed for implementing custom handlers that respond to uuidMetadata event retrieval operations. It defines a single `handle` method that is called with a [PNUUIDMetadataResult](../../com.pubnub.api.models.consumer.objects_api.uuid/-p-n-u-u-i-d-metadata-result/index.md) instance, which contains the uuidMetadata. 

 Usage example: 

```kotlin

OnUuidMetadataHandler handler = pnUUIDMetadataResult -> {
    System.out.println("Received uuid Metadata event: " + pnUUIDMetadataResult.getEvent());
};

```

#### See also

| | |
|---|---|
| [PNUUIDMetadataResult](../../com.pubnub.api.models.consumer.objects_api.uuid/-p-n-u-u-i-d-metadata-result/index.md) | for more information about the channel metadata result provided to this handler. |
