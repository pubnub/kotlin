//[pubnub-core-api](../../../index.md)/[com.pubnub.api.v2.entities](../index.md)/[BaseChannelGroup](index.md)/[subscription](subscription.md)

# subscription

[jvm]\
abstract override fun [subscription](subscription.md)(options: [SubscriptionOptions](../../com.pubnub.api.v2.subscriptions/-subscription-options/index.md)): [Subscription](index.md)

Returns a [Subscription](index.md) that can be used to subscribe to this channel group.

Channel group subscriptions support passing [com.pubnub.api.v2.subscriptions.SubscriptionOptions.receivePresenceEvents](../../com.pubnub.api.v2.subscriptions/-subscription-options/-companion/receive-presence-events.md) in [options](subscription.md) to enable receiving presence events.

[com.pubnub.api.v2.subscriptions.SubscriptionOptions.filter](../../com.pubnub.api.v2.subscriptions/-subscription-options/-companion/filter.md) can be used to filter events delivered to the subscription.

For example, to create a subscription that only listens to presence events:

```kotlin
channelGroup.subscription(SubscriptionOptions.receivePresenceEvents() + SubscriptionOptions.filter { it is PNPresenceEventResult } )
```

*Warning:* if a channel is part of more than one channel group, and you create subscription to both (or more) those groups using a single com.pubnub.api.PubNub instance, you will only receive events for that channel in one channel group subscription.

For example, let's say &quot;channel_1&quot; is part of groups &quot;cg_1&quot; and &quot;cg_2&quot;. If you only subscribe to &quot;cg_1&quot;, or you only subscribe to &quot;cg_2&quot;, you will get all events for &quot;channel_1&quot;. However, if in your app you subscribe to both &quot;cg_1&quot; and &quot;cg_2&quot; at the same time, you will only receive events for &quot;channel_1&quot; in one of those subscriptions, chosen at random.

This limitation is due to how the server manages channels and channel groups.

#### Return

an inactive [Subscription](index.md) to this channel group. You must call Subscription.subscribe to start receiving events.

#### Parameters

jvm

| | |
|---|---|
| options | optional [SubscriptionOptions](../../com.pubnub.api.v2.subscriptions/-subscription-options/index.md). |
