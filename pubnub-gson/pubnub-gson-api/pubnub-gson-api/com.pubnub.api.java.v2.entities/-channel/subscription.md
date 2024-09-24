//[pubnub-gson-api](../../../index.md)/[com.pubnub.api.java.v2.entities](../index.md)/[Channel](index.md)/[subscription](subscription.md)

# subscription

[jvm]\
abstract override fun [subscription](subscription.md)(options: [SubscriptionOptions](../../../../../pubnub-kotlin/pubnub-kotlin-api/pubnub-kotlin-api/com.pubnub.api.v2.subscriptions/-subscription-options/index.md)): [Subscription](../../com.pubnub.api.java.v2.subscriptions/-subscription/index.md)

Returns a [Subscription](../../com.pubnub.api.java.v2.subscriptions/-subscription/index.md) that can be used to subscribe to this channel.

Channel subscriptions support passing [com.pubnub.api.v2.subscriptions.SubscriptionOptions.receivePresenceEvents](../../../../../pubnub-kotlin/pubnub-kotlin-api/pubnub-kotlin-api/com.pubnub.api.v2.subscriptions/-subscription-options/-companion/receive-presence-events.md) in [options](subscription.md) to enable receiving presence events.

[com.pubnub.api.v2.subscriptions.SubscriptionOptions.filter](../../../../../pubnub-kotlin/pubnub-kotlin-api/pubnub-kotlin-api/com.pubnub.api.v2.subscriptions/-subscription-options/-companion/filter.md) can be used to filter events delivered to the subscription.

For example, to create a subscription that only listens to presence events:

```kotlin
channel.subscription(SubscriptionOptions.receivePresenceEvents() + SubscriptionOptions.filter { it is PNPresenceEventResult } )
```

#### Return

an inactive [Subscription](../../com.pubnub.api.java.v2.subscriptions/-subscription/index.md) to this channel. You must call [Subscription.subscribe](../../com.pubnub.api.java.v2.subscriptions/-subscription/subscribe.md) to start receiving events.

#### Parameters

jvm

| | |
|---|---|
| options | optional [SubscriptionOptions](../../../../../pubnub-kotlin/pubnub-kotlin-api/pubnub-kotlin-api/com.pubnub.api.v2.subscriptions/-subscription-options/index.md). |

[jvm]\
abstract fun [subscription](subscription.md)(): [Subscription](../../com.pubnub.api.java.v2.subscriptions/-subscription/index.md)

Returns a [Subscription](../../com.pubnub.api.java.v2.subscriptions/-subscription/index.md) that can be used to subscribe to this channel.

The returned [Subscription](../../com.pubnub.api.java.v2.subscriptions/-subscription/index.md) is initially inactive. You must call [Subscription.subscribe](../../com.pubnub.api.java.v2.subscriptions/-subscription/subscribe.md) on it to start receiving events.

#### Return

An inactive [Subscription](../../com.pubnub.api.java.v2.subscriptions/-subscription/index.md) to this channel.
