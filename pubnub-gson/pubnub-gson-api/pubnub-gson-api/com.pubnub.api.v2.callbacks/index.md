//[pubnub-gson-api](../../index.md)/[com.pubnub.api.v2.callbacks](index.md)

# Package-level declarations

## Types

| Name | Summary |
|---|---|
| [EventEmitter](-event-emitter/index.md) | [jvm]<br>interface [EventEmitter](-event-emitter/index.md) : [BaseEventEmitter](../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.v2.callbacks/-base-event-emitter/index.md)&lt;[T](../../../../pubnub-core/pubnub-core-api/com.pubnub.api.v2.callbacks/-base-event-emitter/index.md)&gt; <br>Interface implemented by objects that are the source of real time events from the PubNub network. |
| [EventListener](-event-listener/index.md) | [jvm]<br>interface [EventListener](-event-listener/index.md) : [BaseEventListener](../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.v2.callbacks/-base-event-listener/index.md)<br>Implement this interface and pass it into [EventEmitter.addListener] to listen for events from the PubNub real-time network. |
| [StatusEmitter](-status-emitter/index.md) | [jvm]<br>interface [StatusEmitter](-status-emitter/index.md) : [BaseStatusEmitter](../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.v2.callbacks/-base-status-emitter/index.md)&lt;[T](../../../../pubnub-core/pubnub-core-api/com.pubnub.api.v2.callbacks/-base-status-emitter/index.md)&gt; <br>Interface implemented by objects that manage the subscription connection to the PubNub network and can be monitored for connection state changes. |
| [StatusListener](-status-listener/index.md) | [jvm]<br>interface [StatusListener](-status-listener/index.md) : [BaseStatusListener](../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.v2.callbacks/-base-status-listener/index.md)<br>Implement this interface and pass it into [com.pubnub.api.v2.callbacks.StatusEmitter.addListener] to listen for PubNub connection status changes. |
