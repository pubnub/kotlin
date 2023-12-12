package com.pubnub.internal.v2.subscription

import com.pubnub.api.PubNub
import com.pubnub.api.models.consumer.pubsub.PNEvent
import com.pubnub.api.v2.callbacks.EventEmitter
import com.pubnub.api.v2.callbacks.EventListener
import com.pubnub.api.v2.subscriptions.Subscription
import com.pubnub.api.v2.subscriptions.SubscriptionCursor
import com.pubnub.api.v2.subscriptions.SubscriptionOptions
import com.pubnub.api.v2.subscriptions.SubscriptionSet
import com.pubnub.internal.v2.callbacks.EventEmitterImpl
import com.pubnub.internal.v2.entities.ChannelGroupName
import com.pubnub.internal.v2.entities.ChannelName
import org.jetbrains.annotations.TestOnly

internal class SubscriptionImpl(
    internal val pubnub: PubNub,
    channels: Set<ChannelName> = emptySet(),
    channelGroups: Set<ChannelGroupName> = emptySet(),
    options: SubscriptionOptions = SubscriptionOptions.Default,
) : Subscription(), EventEmitter {

    internal val channels = channels.toSet()
    internal val channelGroups = channelGroups.toSet()
    private val filters = options.allOptions.filterIsInstance<Filter>()

    @get:TestOnly
    internal val eventEmitter = EventEmitterImpl(pubnub, this::accepts)

    internal fun accepts(event: PNEvent): Boolean {
        return filters.all { it.predicate(event) }
    }

    override fun plus(subscription: Subscription): SubscriptionSet {
        return pubnub.subscriptionSetOf(setOf(this, subscription))
    }

    override fun subscribe(cursor: SubscriptionCursor?) {
        pubnub.subscribe(this, cursor = cursor)
    }

    override fun unsubscribe() {
        pubnub.unsubscribe(this)
    }

    override fun close() {
        unsubscribe()
        eventEmitter.removeAllListeners()
    }

    override fun addListener(listener: EventListener) {
        eventEmitter.addListener(listener)
    }

    override fun removeListener(listener: EventListener) {
        eventEmitter.removeListener(listener)
    }

    override fun removeAllListeners() {
        eventEmitter.removeAllListeners()
    }
}
