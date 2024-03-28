package com.pubnub.api.v2.subscriptions

import com.pubnub.api.v2.callbacks.EventEmitter
import com.pubnub.api.v2.callbacks.EventListener

/**
 * A helper class that manages multiple [Subscription]s that can be added to it.
 *
 * Use the [com.pubnub.api.PubNub.subscriptionSetOf] factory methods to create instances of this interface.
 *
 * Adding multiple `Subscription`s to the set, then calling [subscribe] or [unsubscribe] on the set is more efficient
 * than calling [Subscription.subscribe] on each `Subscription` object separately, as the PubNub client can minimize
 * the number of required reconnections internally.
 *
 * Remember to always [close] the set when you're done using it to avoid memory leaks.
 * Closing the set also closes all `Subscription`s that are part of this set.
 */
interface SubscriptionSet : BaseSubscriptionSet<EventListener, Subscription>, EventEmitter {
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
