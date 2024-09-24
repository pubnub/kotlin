//[pubnub-gson-api](../../../index.md)/[com.pubnub.api.java.v2.callbacks](../index.md)/[EventEmitter](index.md)/[setOnFile](set-on-file.md)

# setOnFile

[jvm]\
abstract fun [setOnFile](set-on-file.md)(onFileHandler: [OnFileHandler](../../com.pubnub.api.java.v2.callbacks.handlers/-on-file-handler/index.md)?)

Sets the handler for incoming file events. This method allows the assignment of an [OnFileHandler](../../com.pubnub.api.java.v2.callbacks.handlers/-on-file-handler/index.md) implementation or lambda expression to handle incoming presence events.

To deactivate the current behavior, simply set this property to `null`.

Note that this property allows for the assignment of a singular behavior at a time, as any new assignment will override the previous one. For scenarios requiring multiple behaviors in response to file events, it is advisable to utilize [EventEmitter.addListener](add-listener.md).

**Setting a Behavior Example:**

<pre>`onFileHandler(pnFileEventResult -> System.out.println("Received: " +  pnFileEventResult.getMessage()));
`</pre> *

**Removing a Behavior Example:**

<pre>`onFileHandler(null);
`</pre> *

#### Parameters

jvm

| | |
|---|---|
| onFileHandler | An implementation of [OnFileHandler](../../com.pubnub.api.java.v2.callbacks.handlers/-on-file-handler/index.md) or a lambda expression to handle incoming messages. It can be `null` to remove the current handler. |
