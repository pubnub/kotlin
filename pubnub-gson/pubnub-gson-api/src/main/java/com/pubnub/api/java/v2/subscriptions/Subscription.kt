package com.pubnub.api.java.v2.subscriptions

import com.pubnub.api.v2.subscriptions.SubscribeCapable

/**
 * Represents a potential subscription to the PubNub real-time network.
 *
 * Create objects of this class through the [com.pubnub.api.v2.entities.Subscribable.subscription] method of the
 * respective entities, such as [com.pubnub.api.java.v2.entities.Channel], [com.pubnub.api.java.v2.entities.ChannelGroup],
 * [com.pubnub.api.java.v2.entities.ChannelMetadata] and [com.pubnub.api.java.v2.entities.UserMetadata].
 *
 * Created subscriptions are initially inactive, which means you must call [subscribe] to start receiving events.
 *
 * This class implements the [AutoCloseable] interface to help you release resources by calling [unsubscribe]
 * and removing all listeners on [close]. Remember to always call [close] when you no longer need this Subscription.
 */
interface Subscription : com.pubnub.api.java.v2.callbacks.EventEmitter, SubscribeCapable, AutoCloseable {
    /**
     * Create a [SubscriptionSet] containing this [Subscription] and the added [subscription].
     */
    operator fun plus(subscription: Subscription): SubscriptionSet

    /**
     * Start receiving events from the subscriptions (or subscriptions) represented by this object.
     *
     * The PubNub client will start a network connection to the server if it doesn't have one already,
     * or will alter the existing connection to add channels and groups requested by this subscriptions if needed.
     */
    fun subscribe()
}
