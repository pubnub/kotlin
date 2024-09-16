package com.pubnub.api.java.v2.entities

import com.pubnub.api.v2.subscriptions.EmptyOptions
import com.pubnub.api.v2.subscriptions.SubscriptionOptions

/**
 * This interface is implemented by entities that can be subscribed to, such as channels, channel groups, and user and
 * channel metadata.
 */
interface Subscribable {
    /**
     * Returns a [com.pubnub.api.v2.subscriptions.Subscription] that can be used to subscribe to this `Subscribable`.
     *
     * @param options optional [SubscriptionOptions].
     * @return an inactive [Subscription] to this `Subscribable`. You must call [Subscription.subscribe] to start receiving events.
     */
    fun subscription(options: SubscriptionOptions = EmptyOptions): com.pubnub.api.java.v2.subscriptions.Subscription
}
