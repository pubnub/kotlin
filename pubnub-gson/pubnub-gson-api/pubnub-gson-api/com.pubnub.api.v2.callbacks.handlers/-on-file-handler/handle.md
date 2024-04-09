//[pubnub-gson-api](../../../index.md)/[com.pubnub.api.v2.callbacks.handlers](../index.md)/[OnFileHandler](index.md)/[handle](handle.md)

# handle

[jvm]\
abstract fun [handle](handle.md)(pnFileEventResult: [PNFileEventResult](../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.models.consumer.pubsub.files/-p-n-file-event-result/index.md))

 This interface is designed for implementing custom handlers that respond to file event retrieval operations. It defines a single `handle` method that is called with a [PNFileEventResult](../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.models.consumer.pubsub.files/-p-n-file-event-result/index.md) instance, which contains the file. 

 Usage example: 

```kotlin

OnFileHandler handler = pnFileEventResult -> {
    System.out.println("Received file event: " + pnFileEventResult.getMessage());
};

```

#### See also

| | |
|---|---|
| [PNFileEventResult](../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.models.consumer.pubsub.files/-p-n-file-event-result/index.md) | for more information about the message result provided to this handler. |
