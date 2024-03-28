//[pubnub-core-api](../../../index.md)/[com.pubnub.api.v2.entities](../index.md)/[BaseChannel](index.md)/[subscription](subscription.md)

# subscription

[jvm]\
abstract override fun [subscription](subscription.md)(options: [SubscriptionOptions](../../com.pubnub.api.v2.subscriptions/-subscription-options/index.md)): [Subscription](index.md)

Returns a [Subscription](index.md) that can be used to subscribe to this channel.

Channel subscriptions support passing [com.pubnub.api.v2.subscriptions.SubscriptionOptions.receivePresenceEvents](../../com.pubnub.api.v2.subscriptions/-subscription-options/-companion/receive-presence-events.md) in [options](subscription.md) to enable receiving presence events.

[com.pubnub.api.v2.subscriptions.SubscriptionOptions.filter](../../com.pubnub.api.v2.subscriptions/-subscription-options/-companion/filter.md) can be used to filter events delivered to the subscription.

For example, to create a subscription that only listens to presence events:

```kotlin
channel.subscription(SubscriptionOptions.receivePresenceEvents() + SubscriptionOptions.filter { it is PNPresenceEventResult } )
```

#### Return

an inactive [Subscription](index.md) to this channel. You must call Subscription.subscribe to start receiving events.

#### Parameters

jvm

| | |
|---|---|
| options | optional [SubscriptionOptions](../../com.pubnub.api.v2.subscriptions/-subscription-options/index.md). |
