//[pubnub-gson](../../../index.md)/[com.pubnub.api.v2.callbacks](../index.md)/[EventEmitter](index.md)/[setOnUuidMetadata](set-on-uuid-metadata.md)

# setOnUuidMetadata

[jvm]\
abstract fun [setOnUuidMetadata](set-on-uuid-metadata.md)(onUuidMetadataHandler: [OnUuidMetadataHandler](../../com.pubnub.api.v2.callbacks.handlers/-on-uuid-metadata-handler/index.md))

Sets the handler for incoming uuidMetadata events. This method allows the assignment of an [OnUuidMetadataHandler](../../com.pubnub.api.v2.callbacks.handlers/-on-uuid-metadata-handler/index.md) implementation or lambda expression to handle incoming presence events. To deactivate the current behavior, simply set this property to `null`. Note that this property allows for the assignment of a singular behavior at a time, as any new assignment will override the previous one. For scenarios requiring multiple behaviors in response to uuidMetadata events, it is advisable to utilize [addListener](index.md#330403064%2FFunctions%2F-395131529). 

Setting a Behavior Example:

```kotlin

onUuidMetadataHandler(pnUUIDMetadataResult -> System.out.println("Received: " + pnUUIDMetadataResult.getData()));

```

Removing a Behavior Example:

```kotlin

onUuidMetadataHandler(null);

```

#### Parameters

jvm

| | |
|---|---|
| onUuidMetadataHandler | An implementation of [OnUuidMetadataHandler](../../com.pubnub.api.v2.callbacks.handlers/-on-uuid-metadata-handler/index.md) or a lambda expression to handle incoming messages. It can be `null` to remove the current handler. |
