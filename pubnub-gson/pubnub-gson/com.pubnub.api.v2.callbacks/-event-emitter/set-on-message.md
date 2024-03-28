//[pubnub-gson](../../../index.md)/[com.pubnub.api.v2.callbacks](../index.md)/[EventEmitter](index.md)/[setOnMessage](set-on-message.md)

# setOnMessage

[jvm]\
abstract fun [setOnMessage](set-on-message.md)(onMessageHandler: [OnMessageHandler](../../com.pubnub.api.v2.callbacks.handlers/-on-message-handler/index.md))

Sets the handler for incoming message events. This method allows the assignment of an [OnMessageHandler](../../com.pubnub.api.v2.callbacks.handlers/-on-message-handler/index.md) implementation or lambda expression to handle incoming messages. To deactivate the current behavior, simply set this property to `null`. Note that this property allows for the assignment of a singular behavior at a time, as any new assignment will override the previous one. For scenarios requiring multiple behaviors in response to message events, it is advisable to utilize [addListener](index.md#330403064%2FFunctions%2F-395131529). 

Setting a Behavior Example:

```kotlin

setOnMessage(pnMessageResult -> System.out.println("Received: " + pnMessageResult.getMessage()));

```

Removing a Behavior Example:

```kotlin

setOnMessage(null);

```

#### Parameters

jvm

| | |
|---|---|
| onMessageHandler | An implementation of [OnMessageHandler](../../com.pubnub.api.v2.callbacks.handlers/-on-message-handler/index.md) or a lambda expression to handle incoming messages. It can be `null` to remove the current handler. |
