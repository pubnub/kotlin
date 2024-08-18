package com.pubnub.internal.v2.subscription

import com.pubnub.api.callbacks.Listener
import com.pubnub.api.models.consumer.pubsub.PNEvent
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult
import com.pubnub.api.models.consumer.pubsub.PNSignalResult
import com.pubnub.api.models.consumer.pubsub.files.PNFileEventResult
import com.pubnub.api.models.consumer.pubsub.message_actions.PNMessageActionResult
import com.pubnub.api.models.consumer.pubsub.objects.PNObjectEventResult
import com.pubnub.api.v2.callbacks.EventListener
import com.pubnub.api.v2.subscriptions.EmptyOptions
import com.pubnub.api.v2.subscriptions.FilterImpl
import com.pubnub.api.v2.subscriptions.Subscription
import com.pubnub.api.v2.subscriptions.SubscriptionCursor
import com.pubnub.api.v2.subscriptions.SubscriptionOptions
import com.pubnub.api.v2.subscriptions.SubscriptionSet
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.managers.AnnouncementCallback
import com.pubnub.internal.managers.AnnouncementEnvelope
import com.pubnub.internal.v2.callbacks.EventEmitterImpl
import com.pubnub.internal.v2.entities.ChannelGroupName
import com.pubnub.internal.v2.entities.ChannelName

interface SubscriptionInternal : Subscription {
    val pubnub: PubNubImpl

    fun onSubscriptionActive(cursor: SubscriptionCursor)

    fun onSubscriptionInactive()

    val channels: Set<ChannelName>
    val channelGroups: Set<ChannelGroupName>
}

open class SubscriptionImpl(
    override val pubnub: PubNubImpl,
    channels: Set<ChannelName> = emptySet(),
    channelGroups: Set<ChannelGroupName> = emptySet(),
    val options: SubscriptionOptions = EmptyOptions,
    eventEmitterFactory: (SubscriptionImpl) -> EventEmitterImpl = { subscriptionImpl ->
        EventEmitterImpl(AnnouncementCallback.Phase.SUBSCRIPTION, subscriptionImpl::accepts)
    }
) : SubscriptionInternal {
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

    override val channels: Set<ChannelName> = channels.toSet()
    override val channelGroups: Set<ChannelGroupName> = channelGroups.toSet()

    private val filters: List<FilterImpl> = options.allOptions.filterIsInstance<FilterImpl>()

    /**
     * To ensure that events are delivered with timestamps growing monotonically,
     * we will set this to the highest received timestamp and compare incoming messages against it.
     *
     * This will be reset on subscribe(cursor) with the value from the SubscriptionCursor.
     */
    private var lastTimetoken: Long = 0L

    protected val eventEmitter = eventEmitterFactory(this)

    fun accepts(envelope: AnnouncementEnvelope<out PNEvent>): Boolean {
        val event = envelope.event
        val accepted = isActive && filters.all { filter -> filter.predicate(event) } && checkAndUpdateTimetoken(event)
        if (accepted) {
            envelope.acceptedBy += this@SubscriptionImpl
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

    override fun subscribe(cursor: SubscriptionCursor) {
        onSubscriptionActive(cursor)
        pubnub.subscribe(this, cursor = cursor)
    }

    override fun onSubscriptionActive(cursor: SubscriptionCursor) {
        lastTimetoken = cursor.timetoken
        isActive = true
    }

    override fun unsubscribe() {
        onSubscriptionInactive()
        pubnub.unsubscribe(this)
    }

    override fun onSubscriptionInactive() {
        lastTimetoken = 0L
        isActive = false
    }

    override fun close() {
        unsubscribe()
    }

    override fun addListener(listener: EventListener) {
        eventEmitter.addListener(listener)
    }

    override fun removeAllListeners() {
        eventEmitter.removeAllListeners()
    }

    override fun removeListener(listener: Listener) {
        eventEmitter.removeListener(listener)
    }

    private val emitterHelper = EmitterHelper(eventEmitter)
    override var onMessage: ((PNMessageResult) -> Unit)? by emitterHelper::onMessage
    override var onPresence: ((PNPresenceEventResult) -> Unit)? by emitterHelper::onPresence
    override var onSignal: ((PNSignalResult) -> Unit)? by emitterHelper::onSignal
    override var onMessageAction: ((PNMessageActionResult) -> Unit)? by emitterHelper::onMessageAction
    override var onObjects: ((PNObjectEventResult) -> Unit)? by emitterHelper::onObjects
    override var onFile: ((PNFileEventResult) -> Unit)? by emitterHelper::onFile

    override fun plus(subscription: Subscription): SubscriptionSet {
        return pubnub.subscriptionSetOf(setOf(this, subscription))
    }
}
