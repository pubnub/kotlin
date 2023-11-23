package com.pubnub.internal.v2

import com.pubnub.api.PubNub
import com.pubnub.api.callbacks.SubscribeCallback
import com.pubnub.api.models.consumer.pubsub.PNEvent
import com.pubnub.api.v2.EventEmitter
import com.pubnub.api.v2.Subscription
import com.pubnub.api.v2.SubscriptionCursor
import com.pubnub.api.v2.SubscriptionOptions
import com.pubnub.api.v2.SubscriptionSet

internal class SubscriptionImpl(
    internal val pubNub: PubNub,
    channels: Set<ChannelName> = emptySet(),
    channelGroups: Set<ChannelGroupName> = emptySet(),
    options: SubscriptionOptions = SubscriptionOptions.Default,
) : Subscription(), EventEmitter {

    internal val channels = channels.toSet()
    internal val channelGroups = channelGroups.toSet()
    private val filters = options.allOptions().filterIsInstance<Filter>()

    private val eventEmitter: EventEmitterImpl = EventEmitterImpl(pubNub, this::accepts)

    internal fun accepts(event: PNEvent): Boolean {
        return filters.all { it.predicate(event) }
    }

    override fun plus(subscription: Subscription): SubscriptionSet {
        return pubNub.subscriptionSetOf(setOf(this, subscription))
    }

    override fun subscribe(cursor: SubscriptionCursor?) {
        pubNub.subscribe(this, cursor = cursor)
    }

    override fun unsubscribe() {
        pubNub.unsubscribe(this)
    }

    override fun close() {
        unsubscribe()
        eventEmitter.removeAllListeners()
    }

    override fun addListener(listener: SubscribeCallback) {
        eventEmitter.addListener(listener)
    }

    override fun removeListener(listener: SubscribeCallback) {
        eventEmitter.removeListener(listener)
    }

    override fun removeAllListeners() {
        eventEmitter.removeAllListeners()
    }
}
