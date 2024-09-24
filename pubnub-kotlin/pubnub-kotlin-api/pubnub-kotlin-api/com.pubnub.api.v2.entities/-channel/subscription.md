//[pubnub-kotlin-api](../../../index.md)/[com.pubnub.api.v2.entities](../index.md)/[Channel](index.md)/[subscription](subscription.md)

# subscription

[common]\
abstract override fun [subscription](subscription.md)(options: [SubscriptionOptions](../../com.pubnub.api.v2.subscriptions/-subscription-options/index.md)): [Subscription](../../com.pubnub.api.v2.subscriptions/-subscription/index.md)

Returns a [Subscription](../../com.pubnub.api.v2.subscriptions/-subscription/index.md) that can be used to subscribe to this channel.

Channel subscriptions support passing [com.pubnub.api.v2.subscriptions.SubscriptionOptions.receivePresenceEvents](../../com.pubnub.api.v2.subscriptions/-subscription-options/-companion/receive-presence-events.md) in [options](subscription.md) to enable receiving presence events.

[com.pubnub.api.v2.subscriptions.SubscriptionOptions.filter](../../com.pubnub.api.v2.subscriptions/-subscription-options/-companion/filter.md) can be used to filter events delivered to the subscription.

For example, to create a subscription that only listens to presence events:

```kotlin
channel.subscription(SubscriptionOptions.receivePresenceEvents() + SubscriptionOptions.filter { it is PNPresenceEventResult } )
```

#### Return

an inactive [Subscription](../../com.pubnub.api.v2.subscriptions/-subscription/index.md) to this channel. You must call [Subscription.subscribe](../../../../../pubnub-kotlin/pubnub-kotlin-api/com.pubnub.api.v2.subscriptions/-subscription/subscribe.md) to start receiving events.

#### Parameters

common

| | |
|---|---|
| options | optional [SubscriptionOptions](../../com.pubnub.api.v2.subscriptions/-subscription-options/index.md). |
