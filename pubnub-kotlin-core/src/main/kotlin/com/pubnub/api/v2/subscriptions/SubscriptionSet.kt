package com.pubnub.api.v2.subscriptions

import com.pubnub.api.v2.callbacks.EventEmitter

/**
 * A helper class that manages multiple [Subscription]s that can be added to it.
 *
 * Use the [com.pubnub.api.PubNub.subscriptionSetOf] factory methods to create instances of this class.
 *
 * Adding multiple `Subscription`s to the set, then calling [subscribe] or [unsubscribe] on the set is more efficient
 * than calling `subscribe` on all `Subscription` objects separately, as the PubNub client can minimize the number of
 * required reconnections internally.
 *
 * Remember to always [close] the set when you're done with it to avoid memory leaks.
 * Closing the set also closes all `Subscription`s that are part of this set.
 */
abstract class SubscriptionSet internal constructor() : EventEmitter, SubscribeCapable, AutoCloseable {
    /**
     * Add a [Subscription] to this set.
     *
     * Please note that this [SubscriptionSet] will *not* attempt to ensure all subscriptions match their
     * active/inactive state. That is, if you previously called [subscribe] or [unsubscribe] on this set, it will not be
     * called on the newly added [subscription] automatically.
     *
     * @param subscription the [Subscription] to add.
     * @see [plus]
     */
    abstract fun add(subscription: Subscription)

    /**
     * Remove a [Subscription] from this set.
     *
     * Please note that removing a subscription from the set does not automatically [unsubscribe] or [close] it.
     *
     * @param subscription the [Subscription] to remove.
     */
    abstract fun remove(subscription: Subscription)

    /**
     * Remove a [Subscription] from this set. Equivalent to calling [remove].
     *
     * Please note that removing a subscription from the set does not automatically [unsubscribe] or [close] it.
     *
     * @see [remove]
     */
    operator fun minus(subscription: Subscription) = remove(subscription)

    /**
     * Adds a [Subscription] to this set. Equivalent to calling [add].
     *
     * Please note that this [SubscriptionSet] will *not* attempt to ensure all subscriptions match their
     * active/inactive state. That is, if you previously called [subscribe] or [unsubscribe] on this set, it will not be
     * called on the newly added [subscription] automatically.
     *
     * @param subscription the [Subscription] to add.
     * @see [add]
     */
    abstract operator fun plus(subscription: Subscription): SubscriptionSet

    /**
     * Returns an immutable copy of the set of subscriptions contained in this [SubscriptionSet].
     */
    abstract val subscriptions: Set<Subscription>
}
