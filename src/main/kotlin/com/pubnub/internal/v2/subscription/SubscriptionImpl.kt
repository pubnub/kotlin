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

    // helps deliver events only after subscribe was called, starting AT LEAST with the requested cursor.timetoken
    internal var deliverEventsFrom: SubscriptionCursor? = null

    internal fun accepts(event: PNEvent): Boolean {
        val cursorTimetoken = deliverEventsFrom?.timetoken ?: return false
        val eventTimetoken = event.timetoken ?: return false

        return cursorTimetoken <= eventTimetoken &&
            filters.all { filter -> filter.predicate(event) }
    }

    override fun plus(subscription: Subscription): SubscriptionSet {
        return pubnub.subscriptionSetOf(setOf(this, subscription))
    }

    override fun subscribe(cursor: SubscriptionCursor) {
        deliverEventsFrom = cursor
        pubnub.subscribe(this, cursor = cursor)
    }

    override fun unsubscribe() {
        deliverEventsFrom = null
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
