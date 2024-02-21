package com.pubnub.internal.v2.subscription

import com.pubnub.api.callbacks.Listener
import com.pubnub.api.models.consumer.pubsub.PNEvent
import com.pubnub.api.v2.callbacks.BaseEventEmitter
import com.pubnub.api.v2.callbacks.BaseEventListener
import com.pubnub.api.v2.subscriptions.BaseSubscription
import com.pubnub.api.v2.subscriptions.BaseSubscriptionSet
import com.pubnub.api.v2.subscriptions.FilterImpl
import com.pubnub.api.v2.subscriptions.SubscriptionCursor
import com.pubnub.api.v2.subscriptions.SubscriptionOptions
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.managers.AnnouncementCallback
import com.pubnub.internal.managers.AnnouncementEnvelope
import com.pubnub.internal.v2.callbacks.EventEmitterImpl
import com.pubnub.internal.v2.entities.ChannelGroupName
import com.pubnub.internal.v2.entities.ChannelName

open class BaseSubscriptionImpl(
    internal val pubnub: PubNubImpl,
    channels: Set<ChannelName> = emptySet(),
    channelGroups: Set<ChannelGroupName> = emptySet(),
    options: SubscriptionOptions? = null,
) : BaseSubscription(), BaseEventEmitter {

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

    internal val channels = channels.toSet()
    internal val channelGroups = channelGroups.toSet()

    private val filters: List<FilterImpl> = options?.allOptions?.filterIsInstance<FilterImpl>() ?: emptyList()

    /**
     * To ensure that events are delivered with timestamps growing monotonically,
     * we will set this to the highest received timestamp and compare incoming messages against it.
     *
     * This will be reset on subscribe(cursor) with the value from the SubscriptionCursor.
     */
    private var lastTimetoken: Long = 0L

    protected val eventEmitter = EventEmitterImpl(AnnouncementCallback.Phase.SUBSCRIPTION, ::accepts)
    private fun accepts(envelope: AnnouncementEnvelope<out PNEvent>): Boolean {
        val event = envelope.event
        val accepted = isActive && filters.all { filter -> filter.predicate(event) } && checkAndUpdateTimetoken(event)
        if (accepted) {
            envelope.acceptedBy += this@BaseSubscriptionImpl
        }
        return accepted
    }

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

    override fun plus(subscription: BaseSubscription): BaseSubscriptionSet {
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
    }

    override fun addListener(listener: BaseEventListener) {
        eventEmitter.addListener(listener)
    }

    override fun removeListener(listener: Listener) {
        eventEmitter.removeListener(listener)
    }

    override fun removeAllListeners() {
        eventEmitter.removeAllListeners()
    }
}
