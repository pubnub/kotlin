//[pubnub-gson](../../../index.md)/[com.pubnub.api.v2.callbacks](../index.md)/[EventEmitter](index.md)/[setOnMessageAction](set-on-message-action.md)

# setOnMessageAction

[jvm]\
abstract fun [setOnMessageAction](set-on-message-action.md)(onMessageActionHandler: [OnMessageActionHandler](../../com.pubnub.api.v2.callbacks.handlers/-on-message-action-handler/index.md))

Sets the handler for incoming messageAction events. This method allows the assignment of an [OnMessageActionHandler](../../com.pubnub.api.v2.callbacks.handlers/-on-message-action-handler/index.md) implementation or lambda expression to handle incoming presence events. To deactivate the current behavior, simply set this property to `null`. Note that this property allows for the assignment of a singular behavior at a time, as any new assignment will override the previous one. For scenarios requiring multiple behaviors in response to messageAction events, it is advisable to utilize [addListener](index.md#330403064%2FFunctions%2F-395131529). 

Setting a Behavior Example:

```kotlin

onMessageActionHandler(pnMessageActionResult -> System.out.println("Received: " + pnMessageActionResult.getMessageAction()));

```

Removing a Behavior Example:

```kotlin

onMessageActionHandler(null);

```

#### Parameters

jvm

| | |
|---|---|
| onMessageActionHandler | An implementation of [OnMessageActionHandler](../../com.pubnub.api.v2.callbacks.handlers/-on-message-action-handler/index.md) or a lambda expression to handle incoming messages. It can be `null` to remove the current handler. |
