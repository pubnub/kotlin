//[pubnub-gson-api](../../../index.md)/[com.pubnub.api.v2.callbacks](../index.md)/[EventEmitter](index.md)/[setOnMembership](set-on-membership.md)

# setOnMembership

[jvm]\
abstract fun [setOnMembership](set-on-membership.md)(onMembershipHandler: [OnMembershipHandler](../../com.pubnub.api.v2.callbacks.handlers/-on-membership-handler/index.md))

Sets the handler for incoming membership events. This method allows the assignment of an [OnMembershipHandler](../../com.pubnub.api.v2.callbacks.handlers/-on-membership-handler/index.md) implementation or lambda expression to handle incoming presence events. To deactivate the current behavior, simply set this property to `null`. Note that this property allows for the assignment of a singular behavior at a time, as any new assignment will override the previous one. For scenarios requiring multiple behaviors in response to membership events, it is advisable to utilize [addListener](index.md#330403064%2FFunctions%2F126356644). 

Setting a Behavior Example:

```kotlin

onMembershipHandler(pnMembershipResult -> System.out.println("Received: " +  pnMembershipResult.getEvent()));

```

Removing a Behavior Example:

```kotlin

onMembershipHandler(null);

```

#### Parameters

jvm

| | |
|---|---|
| onMembershipHandler | An implementation of [OnMembershipHandler](../../com.pubnub.api.v2.callbacks.handlers/-on-membership-handler/index.md) or a lambda expression to handle incoming messages. It can be `null` to remove the current handler. |
