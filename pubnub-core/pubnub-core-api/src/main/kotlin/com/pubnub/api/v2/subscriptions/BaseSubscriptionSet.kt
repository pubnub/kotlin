package com.pubnub.api.v2.subscriptions

import com.pubnub.api.v2.callbacks.BaseEventEmitter
import com.pubnub.api.v2.callbacks.BaseEventListener

interface BaseSubscriptionSet<
    EvLis : BaseEventListener,
    Subscription : BaseSubscription<EvLis>,
    > : BaseEventEmitter<EvLis>, SubscribeCapable, AutoCloseable {
    /**
     * Add a [Subscription] to this set.
     *
     * Please note that this SubscriptionSet will *not* attempt to ensure all subscriptions match their
     * active/inactive state. That is, if you previously called [subscribe] or [unsubscribe] on this set, it will not be
     * called on the newly added [subscription] automatically.
     *
     * @param subscription the [Subscription] to add.
     */
    fun add(subscription: Subscription)

    /**
     * Remove the [subscription] from this set.
     *
     * Please note that removing a subscription from the set does not automatically [unsubscribe] or [close] it.
     *
     * @param subscription the [Subscription] to remove.
     */
    fun remove(subscription: Subscription)

    /**
     * Returns an immutable copy of the set of subscriptions contained in this [BaseSubscriptionSet].
     */
    val subscriptions: Set<Subscription>
}
