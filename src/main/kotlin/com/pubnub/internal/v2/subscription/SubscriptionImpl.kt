package com.pubnub.internal.v2.subscription

import com.pubnub.api.PubNub
import com.pubnub.api.callbacks.Listener
import com.pubnub.api.managers.AnnouncementCallback
import com.pubnub.api.managers.AnnouncementEnvelope
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

internal class SubscriptionImpl(
    internal val pubnub: PubNub,
    channels: Set<ChannelName> = emptySet(),
    channelGroups: Set<ChannelGroupName> = emptySet(),
    options: SubscriptionOptions? = null,
) : Subscription(), EventEmitter {

    @Volatile
    var isActive = false
        @Synchronized
        set(newValue) {
            if (!field && newValue) { // wasn't active && is now active
                pubnub.listenerManager.addAnnouncementCallback(eventEmitter)
            } else if (field && !newValue) { // was active && no longer active
                pubnub.listenerManager.removeAnnouncementCallback(eventEmitter)
            }
            field = newValue
        }

    private val eventEmitter = EventEmitterImpl(AnnouncementCallback.Phase.SUBSCRIPTION, ::accepts)

    internal val channels = channels.toSet()
    internal val channelGroups = channelGroups.toSet()

    private val filters: List<FilterImpl> = options?.allOptions?.filterIsInstance<FilterImpl>() ?: emptyList()

    /**
     * To ensure that events are delivered with timestamps growing monotonically,
     * we will set this to the highest received timestamp and compare incoming messages against it.
     */
    private var lastTimetoken: Long = 0L

    private fun checkAndUpdateTimetoken(result: PNEvent): Boolean {
        result.timetoken?.let { resultTimetoken ->
            if (resultTimetoken <= lastTimetoken) {
                return false
            } else {
                lastTimetoken = resultTimetoken
                return true
            }
        } ?: return false
    }

    private fun accepts(envelope: AnnouncementEnvelope<out PNEvent>): Boolean {
        val event = envelope.event
        val accepted = isActive && filters.all { filter -> filter.predicate(event) } && checkAndUpdateTimetoken(event)
        if (accepted) {
            envelope.acceptedBy += this@SubscriptionImpl
        }
        return accepted
    }

    override fun plus(subscription: Subscription): SubscriptionSet {
        return pubnub.subscriptionSetOf(setOf(this, subscription))
    }

    override fun subscribe(cursor: SubscriptionCursor) {
        onSubscriptionActive(cursor)
        pubnub.subscribe(this, cursor = cursor)
    }

    internal fun onSubscriptionActive(cursor: SubscriptionCursor) {
        lastTimetoken = cursor.timetoken
        isActive = true
    }

    override fun unsubscribe() {
        onSubscriptionInactive()
        pubnub.unsubscribe(this)
    }

    internal fun onSubscriptionInactive() {
        lastTimetoken = 0L
        isActive = false
    }

    override fun close() {
        unsubscribe()
        eventEmitter.removeAllListeners()
    }

    override fun addListener(listener: EventListener) {
        eventEmitter.addListener(listener)
    }

    override fun removeListener(listener: Listener) {
        eventEmitter.removeListener(listener)
    }

    override fun removeAllListeners() {
        eventEmitter.removeAllListeners()
    }
}
