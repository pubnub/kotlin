package com.pubnub.api.v2.subscriptions

import com.pubnub.api.v2.callbacks.BaseEventEmitter

/**
 * Represents a potential subscription to [com.pubnub.api.PubNub].
 *
 * Create objects of this class through the [com.pubnub.api.v2.entities.Subscribable.subscription] method of the
 * respective entities, such as [com.pubnub.api.v2.entities.BaseChannel], [com.pubnub.api.v2.entities.BaseChannelGroup],
 * [com.pubnub.api.v2.entities.BaseChannelMetadata] and [com.pubnub.api.v2.entities.BaseUserMetadata].
 *
 * Created subscriptions are initially inactive, which means you must call [subscribe] to start receiving events.
 *
 * This class implements the [AutoCloseable] interface to help you release resources by calling [unsubscribe]
 * and removing all listeners on [close]. Remember to always call [close] when you no longer need this Subscription.
 */
abstract class BaseSubscription() : BaseEventEmitter, SubscribeCapable, AutoCloseable {
    /**
     * Create a [BaseSubscriptionSet] that contains both subscriptions.
     *
     * @param subscription the other [BaseSubscription] to add to the [BaseSubscriptionSet]
     */
    abstract operator fun plus(subscription: BaseSubscription): BaseSubscriptionSet
}
