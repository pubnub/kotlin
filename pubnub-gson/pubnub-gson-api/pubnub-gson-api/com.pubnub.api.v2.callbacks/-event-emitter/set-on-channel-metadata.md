//[pubnub-gson-api](../../../index.md)/[com.pubnub.api.v2.callbacks](../index.md)/[EventEmitter](index.md)/[setOnChannelMetadata](set-on-channel-metadata.md)

# setOnChannelMetadata

[jvm]\
abstract fun [setOnChannelMetadata](set-on-channel-metadata.md)(onChannelMetadataHandler: [OnChannelMetadataHandler](../../com.pubnub.api.v2.callbacks.handlers/-on-channel-metadata-handler/index.md))

Sets the handler for incoming channelMetadata events. This method allows the assignment of an [OnChannelMetadataHandler](../../com.pubnub.api.v2.callbacks.handlers/-on-channel-metadata-handler/index.md) implementation or lambda expression to handle incoming presence events. To deactivate the current behavior, simply set this property to `null`. Note that this property allows for the assignment of a singular behavior at a time, as any new assignment will override the previous one. For scenarios requiring multiple behaviors in response to channelMetadata events, it is advisable to utilize [addListener](index.md#330403064%2FFunctions%2F126356644). 

Setting a Behavior Example:

```kotlin

onChannelMetadataHandler(pnChannelMetadataResult -> System.out.println("Received: " +  pnChannelMetadataResult.getEvent()));

```

Removing a Behavior Example:

```kotlin

onChannelMetadataHandler(null);

```

#### Parameters

jvm

| | |
|---|---|
| onChannelMetadataHandler | An implementation of [OnChannelMetadataHandler](../../com.pubnub.api.v2.callbacks.handlers/-on-channel-metadata-handler/index.md) or a lambda expression to handle incoming messages. It can be `null` to remove the current handler. |
