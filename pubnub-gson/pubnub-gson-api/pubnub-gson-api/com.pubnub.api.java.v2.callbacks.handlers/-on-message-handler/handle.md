//[pubnub-gson-api](../../../index.md)/[com.pubnub.api.java.v2.callbacks.handlers](../index.md)/[OnMessageHandler](index.md)/[handle](handle.md)

# handle

[jvm]\
abstract fun [handle](handle.md)(pnMessageResult: [PNMessageResult](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.models.consumer.pubsub/-p-n-message-result/index.md))

 This interface is designed for implementing custom handlers that respond to message retrieval operations. It defines a single `handle` method that is called with a [PNMessageResult](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.models.consumer.pubsub/-p-n-message-result/index.md) instance, which contains the message. 

 Usage example: 

```kotlin

OnMessageHandler handler = pnMessageResult -> {
    System.out.println("Received message: " + pnMessageResult.getMessage());
};

```

#### See also

| | |
|---|---|
| [PNMessageResult](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.models.consumer.pubsub/-p-n-message-result/index.md) | for more information about the message result provided to this handler. |
