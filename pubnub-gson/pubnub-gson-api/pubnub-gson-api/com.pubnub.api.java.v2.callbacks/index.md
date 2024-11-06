//[pubnub-gson-api](../../index.md)/[com.pubnub.api.java.v2.callbacks](index.md)

# Package-level declarations

## Types

| Name | Summary |
|---|---|
| [EventEmitter](-event-emitter/index.md) | [jvm]<br>interface [EventEmitter](-event-emitter/index.md)<br>Interface implemented by objects that are the source of real time events from the PubNub network. |
| [EventListener](-event-listener/index.md) | [jvm]<br>interface [EventListener](-event-listener/index.md) : [Listener](../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.callbacks/-listener/index.md)<br>Implement this interface and pass it into [EventEmitter.addListener](-event-emitter/add-listener.md) to listen for events from the PubNub real-time network. |
| [StatusEmitter](-status-emitter/index.md) | [jvm]<br>interface [StatusEmitter](-status-emitter/index.md)<br>Interface implemented by objects that manage the subscription connection to the PubNub network and can be monitored for connection state changes. |
| [StatusListener](-status-listener/index.md) | [jvm]<br>fun interface [StatusListener](-status-listener/index.md) : [Listener](../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.callbacks/-listener/index.md) |
