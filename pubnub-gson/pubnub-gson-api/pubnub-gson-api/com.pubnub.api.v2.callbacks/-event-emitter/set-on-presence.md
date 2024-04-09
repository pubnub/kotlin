//[pubnub-gson-api](../../../index.md)/[com.pubnub.api.v2.callbacks](../index.md)/[EventEmitter](index.md)/[setOnPresence](set-on-presence.md)

# setOnPresence

[jvm]\
abstract fun [setOnPresence](set-on-presence.md)(onPresenceHandler: [OnPresenceHandler](../../com.pubnub.api.v2.callbacks.handlers/-on-presence-handler/index.md))

Sets the handler for incoming presence events. This method allows the assignment of an [OnPresenceHandler](../../com.pubnub.api.v2.callbacks.handlers/-on-presence-handler/index.md) implementation or lambda expression to handle incoming presence events. To deactivate the current behavior, simply set this property to `null`. Note that this property allows for the assignment of a singular behavior at a time, as any new assignment will override the previous one. For scenarios requiring multiple behaviors in response to presence events, it is advisable to utilize [addListener](index.md#330403064%2FFunctions%2F126356644). 

Setting a Behavior Example:

```kotlin

onPresenceHandler(pnPresenceEventResult -> System.out.println("Received: " + pnPresenceEventResult.getEvent()));

```

Removing a Behavior Example:

```kotlin

onPresenceHandler(null);

```

#### Parameters

jvm

| | |
|---|---|
| onPresenceHandler | An implementation of [OnPresenceHandler](../../com.pubnub.api.v2.callbacks.handlers/-on-presence-handler/index.md) or a lambda expression to handle incoming messages. It can be `null` to remove the current handler. |
