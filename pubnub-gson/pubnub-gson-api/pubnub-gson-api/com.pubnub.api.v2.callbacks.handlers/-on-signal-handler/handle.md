//[pubnub-gson-api](../../../index.md)/[com.pubnub.api.v2.callbacks.handlers](../index.md)/[OnSignalHandler](index.md)/[handle](handle.md)

# handle

[jvm]\
abstract fun [handle](handle.md)(pnSignalResult: [PNSignalResult](../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.models.consumer.pubsub/-p-n-signal-result/index.md))

 This interface is designed for implementing custom handlers that respond to message retrieval operations. It defines a single `handle` method that is called with a [PNSignalResult](../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.models.consumer.pubsub/-p-n-signal-result/index.md) instance, which contains the signal. 

 Usage example: 

```kotlin

OnSignalHandler handler = pnSignalResult -> {
    System.out.println("Received message: " + pnSignalResult.getMessage());
};

```

#### See also

| | |
|---|---|
| [PNSignalResult](../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.models.consumer.pubsub/-p-n-signal-result/index.md) | for more information about the message result provided to this handler. |
