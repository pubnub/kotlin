package com.pubnub.api.java.v2.subscriptions

import com.pubnub.api.java.v2.callbacks.EventEmitter
import com.pubnub.api.v2.subscriptions.SubscribeCapable

/**
 * A helper class that manages multiple [Subscription]s that can be added to it.
 *
 * Use the [com.pubnub.api.java.PubNub.subscriptionSetOf] factory methods to create instances of this interface.
 *
 * Adding multiple `Subscription`s to the set, then calling [subscribe] or [unsubscribe] on the set is more efficient
 * than calling [Subscription.subscribe] on each `Subscription` object separately, as the PubNub client can minimize
 * the number of required reconnections internally.
 *
 * Remember to always [close] the set when you're done using it to avoid memory leaks.
 * Closing the set also closes all `Subscription`s that are part of this set.
 */
interface SubscriptionSet : EventEmitter, SubscribeCapable, AutoCloseable {
    /**
     * Start receiving events from the subscriptions (or subscriptions) represented by this object.
     *
     * The PubNub client will start a network connection to the server if it doesn't have one already,
     * or will alter the existing connection to add channels and groups requested by this subscriptions if needed.
     */
    fun subscribe()

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
     * Returns an immutable copy of the set of subscriptions contained in this [SubscriptionSet].
     */
    val subscriptions: Set<Subscription>

    /**
     * Add the [subscription] to this SubscriptionSet. Equivalent to calling [add].
     *
     * @see [add]
     */
    operator fun plusAssign(subscription: Subscription)

    /**
     * Remove the [subscription] from this set. Equivalent to calling [remove].
     *
     * @see [remove]
     */
    operator fun minusAssign(subscription: Subscription)
}
