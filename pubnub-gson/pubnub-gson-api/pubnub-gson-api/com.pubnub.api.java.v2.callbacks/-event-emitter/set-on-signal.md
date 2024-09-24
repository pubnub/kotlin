//[pubnub-gson-api](../../../index.md)/[com.pubnub.api.java.v2.callbacks](../index.md)/[EventEmitter](index.md)/[setOnSignal](set-on-signal.md)

# setOnSignal

[jvm]\
abstract fun [setOnSignal](set-on-signal.md)(onSignalHandler: [OnSignalHandler](../../com.pubnub.api.java.v2.callbacks.handlers/-on-signal-handler/index.md)?)

Sets the handler for incoming signal events. This method allows the assignment of an [OnSignalHandler](../../com.pubnub.api.java.v2.callbacks.handlers/-on-signal-handler/index.md) implementation or lambda expression to handle incoming signals.

To deactivate the current behavior, simply set this property to `null`.

Note that this property allows for the assignment of a singular behavior at a time, as any new assignment will override the previous one. For scenarios requiring multiple behaviors in response to signal events, it is advisable to utilize [EventEmitter.addListener](add-listener.md).

**Setting a Behavior Example:**

<pre>`setOnSignal(pnSignalResult -> System.out.println("Received: " + pnSignalResult.getMessage()));
`</pre> *

**Removing a Behavior Example:**

<pre>`setOnSignal(null);
`</pre> *

#### Parameters

jvm

| | |
|---|---|
| onSignalHandler | An implementation of [OnSignalHandler](../../com.pubnub.api.java.v2.callbacks.handlers/-on-signal-handler/index.md) or a lambda expression to handle incoming messages. It can be `null` to remove the current handler. |
