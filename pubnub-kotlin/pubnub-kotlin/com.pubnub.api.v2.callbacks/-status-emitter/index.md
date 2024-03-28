//[pubnub-kotlin](../../../index.md)/[com.pubnub.api.v2.callbacks](../index.md)/[StatusEmitter](index.md)

# StatusEmitter

[jvm]\
interface [StatusEmitter](index.md) : [BaseStatusEmitter](../../../../pubnub-gson/com.pubnub.api.v2.callbacks/-base-status-emitter/index.md)&lt;[StatusListener](../-status-listener/index.md)&gt; 

Interface implemented by objects that manage the subscription connection to the PubNub network and can be monitored for connection state changes.

## Functions

| Name | Summary |
|---|---|
| [addListener](index.md#1991821895%2FFunctions%2F51989805) | [jvm]<br>abstract fun [addListener](index.md#1991821895%2FFunctions%2F51989805)(listener: [StatusListener](../-status-listener/index.md)) |
| [removeAllListeners](index.md#-960759141%2FFunctions%2F51989805) | [jvm]<br>abstract fun [removeAllListeners](index.md#-960759141%2FFunctions%2F51989805)() |
| [removeListener](index.md#-1789774638%2FFunctions%2F51989805) | [jvm]<br>abstract fun [removeListener](index.md#-1789774638%2FFunctions%2F51989805)(listener: [Listener](../../../../pubnub-gson/com.pubnub.api.callbacks/-listener/index.md)) |
