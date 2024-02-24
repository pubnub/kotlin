package com.pubnub.api.v2.subscriptions

import com.pubnub.api.v2.callbacks.BaseEventEmitter
import com.pubnub.api.v2.callbacks.BaseEventListener

/**
 * Represents a potential subscriptions to [com.pubnub.api.PubNub].
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
interface BaseSubscription<EvLis: BaseEventListener> : BaseEventEmitter<EvLis>, SubscribeCapable, AutoCloseable
