package com.pubnub.api.v2.subscriptions

import com.pubnub.api.v2.callbacks.BaseEventEmitter
import com.pubnub.api.v2.callbacks.BaseEventListener

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
