//[pubnub-gson](../../../index.md)/[com.pubnub.api.v2.callbacks.handlers](../index.md)/[OnMessageActionHandler](index.md)/[handle](handle.md)

# handle

[jvm]\
abstract fun [handle](handle.md)(pnMessageActionResult: [PNMessageActionResult](../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.models.consumer.pubsub.message_actions/-p-n-message-action-result/index.md))

 This interface is designed for implementing custom handlers that respond to messageAction event retrieval operations. It defines a single `handle` method that is called with a [PNMessageActionResult](../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.models.consumer.pubsub.message_actions/-p-n-message-action-result/index.md) instance, which contains the messageAction. 

 Usage example: 

```kotlin

OnMessageActionHandler handler = pnMessageActionResult -> {
    System.out.println("Received messageAction event: " + pnMessageActionResult.getMessageAction());
};

```

#### See also

| | |
|---|---|
| [PNMessageActionResult](../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.models.consumer.pubsub.message_actions/-p-n-message-action-result/index.md) | for more information about the message result provided to this handler. |
