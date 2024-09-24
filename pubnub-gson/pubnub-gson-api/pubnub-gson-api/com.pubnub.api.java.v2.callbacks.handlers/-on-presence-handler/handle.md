//[pubnub-gson-api](../../../index.md)/[com.pubnub.api.java.v2.callbacks.handlers](../index.md)/[OnPresenceHandler](index.md)/[handle](handle.md)

# handle

[jvm]\
abstract fun [handle](handle.md)(pnPresenceEventResult: [PNPresenceEventResult](../../../../../pubnub-kotlin/pubnub-kotlin-api/pubnub-kotlin-api/com.pubnub.api.models.consumer.pubsub/-p-n-presence-event-result/index.md))

 This interface is designed for implementing custom handlers that respond to presence event retrieval operations. It defines a single `handle` method that is called with a [PNPresenceEventResult](../../../../../pubnub-kotlin/pubnub-kotlin-api/pubnub-kotlin-api/com.pubnub.api.models.consumer.pubsub/-p-n-presence-event-result/index.md) instance, which contains the presence data. 

 Usage example: 

```kotlin

OnPresenceHandler handler = pnPresenceEventResult -> {
    System.out.println("Received presence event: " + pnPresenceEventResult.getEvent());
};

```

#### See also

| | |
|---|---|
| [PNPresenceEventResult](../../../../../pubnub-kotlin/pubnub-kotlin-api/pubnub-kotlin-api/com.pubnub.api.models.consumer.pubsub/-p-n-presence-event-result/index.md) | for more information about the message result provided to this handler. |
