package com.pubnub.api.v2.subscriptions

import com.pubnub.api.v2.callbacks.EventEmitter

/**
 * Represents a potential subscription to [com.pubnub.api.PubNub].
 *
 * Create objects of this class through the [com.pubnub.api.v2.entities.Subscribable.subscription] method of the
 * respective entities, such as [com.pubnub.api.v2.entities.Channel], [com.pubnub.api.v2.entities.ChannelGroup],
 * [com.pubnub.api.v2.entities.ChannelMetadata] and [com.pubnub.api.v2.entities.UuidMetadata].
 *
 * Created subscriptions are usually inactive, and you must call [subscribe] to start receiving events.
 *
 * Remember to always [unsubscribe] when you are done with this object.
 * This class implements the [AutoCloseable] interface to help you release resources by unsubscribing and removing all listeners.
 */
abstract class Subscription internal constructor() : EventEmitter, SubscribeCapable, AutoCloseable {
    abstract operator fun plus(subscription: Subscription): SubscriptionSet
}
