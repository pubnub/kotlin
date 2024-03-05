package com.pubnub.api.v2.subscriptions

import com.pubnub.api.v2.callbacks.BaseEventEmitter
import com.pubnub.api.v2.callbacks.BaseEventListener

/**
 * A helper class that manages multiple [BaseSubscription]s that can be added to it.
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
interface BaseSubscriptionSet<EvLis : BaseEventListener, Sub : BaseSubscription<EvLis>> : BaseEventEmitter<EvLis>, SubscribeCapable, AutoCloseable {
    /**
     * Add a [BaseSubscription] to this set.
     *
     * Please note that this [BaseSubscriptionSet] will *not* attempt to ensure all subscriptions match their
     * active/inactive state. That is, if you previously called [subscribe] or [unsubscribe] on this set, it will not be
     * called on the newly added [subscription] automatically.
     *
     * @param subscription the [BaseSubscription] to add.
     * @see [plus]
     */
    fun add(subscription: Sub)

    /**
     * Remove a [BaseSubscription] from this set.
     *
     * Please note that removing a subscriptions from the set does not automatically [unsubscribe] or [close] it.
     *
     * @param subscription the [BaseSubscription] to remove.
     */
    fun remove(subscription: Sub)

    /**
     * Remove a [BaseSubscription] from this set. Equivalent to calling [remove].
     *
     * Please note that removing a subscriptions from the set does not automatically [unsubscribe] or [close] it.
     *
     * @see [remove]
     */
    operator fun minus(subscription: Sub) = remove(subscription)

    /**
     * Returns an immutable copy of the set of subscriptions contained in this [BaseSubscriptionSet].
     */
    val subscriptions: Set<Sub>
}
