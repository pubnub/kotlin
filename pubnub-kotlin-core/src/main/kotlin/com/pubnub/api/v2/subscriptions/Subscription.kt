package com.pubnub.api.v2.subscriptions

import com.pubnub.api.v2.callbacks.EventEmitter

/**
 * Represents a potential subscription to [com.pubnub.api.PubNub].
 *
 * Create objects of this class through the [com.pubnub.api.v2.entities.Subscribable.subscription] method of the
 * respective entities, such as [com.pubnub.api.v2.entities.Channel], [com.pubnub.api.v2.entities.ChannelGroup],
 * [com.pubnub.api.v2.entities.ChannelMetadata] and [com.pubnub.api.v2.entities.UserMetadata].
 *
 * Created subscriptions are initially inactive, which means you must call [subscribe] to start receiving events.
 *
 * This class implements the [AutoCloseable] interface to help you release resources by calling [unsubscribe]
 * and removing all listeners on [close]. Remember to always call [close] when you no longer need this Subscription.
 */
abstract class Subscription internal constructor() : EventEmitter, SubscribeCapable, AutoCloseable {
    /**
     * Create a [SubscriptionSet] that contains both subscriptions.
     *
     * @param subscription the other [Subscription] to add to the [SubscriptionSet]
     */
    abstract operator fun plus(subscription: Subscription): SubscriptionSet
}
