package com.pubnub.internal.v2.subscription

import com.pubnub.api.callbacks.Listener
import com.pubnub.api.callbacks.SubscribeCallback
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult
import com.pubnub.api.models.consumer.pubsub.PNSignalResult
import com.pubnub.api.models.consumer.pubsub.files.PNFileEventResult
import com.pubnub.api.models.consumer.pubsub.message_actions.PNMessageActionResult
import com.pubnub.api.models.consumer.pubsub.objects.PNObjectEventResult
import com.pubnub.api.v2.callbacks.EventListener
import com.pubnub.api.v2.subscriptions.BaseSubscription
import com.pubnub.api.v2.subscriptions.BaseSubscriptionSet
import com.pubnub.api.v2.subscriptions.Subscription
import com.pubnub.api.v2.subscriptions.SubscriptionOptions
import com.pubnub.api.v2.subscriptions.SubscriptionSet
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.v2.callbacks.DelegatingEventListener
import com.pubnub.internal.v2.callbacks.DelegatingSubscribeCallback
import com.pubnub.internal.v2.entities.ChannelGroupName
import com.pubnub.internal.v2.entities.ChannelName

class SubscriptionImpl(
    private val pubnub: PubNubImpl,
    channels: Set<ChannelName>,
    channelGroups: Set<ChannelGroupName>,
    options: SubscriptionOptions,
) : Subscription, BaseSubscriptionImpl<EventListener>(pubnub.pubNubCore, channels, channelGroups, options) {
    override fun addListener(listener: EventListener) {
        addListener(DelegatingEventListener(listener))
    }

    override fun removeListener(listener: Listener) {
        when (listener) {
            is SubscribeCallback -> {
                super.removeListener(DelegatingSubscribeCallback(listener))
            }

            is EventListener -> {
                super.removeListener(DelegatingEventListener(listener))
            }

            else -> {
                super.removeListener(listener)
            }
        }
    }

    private val emitterHelper = EmitterHelper(eventEmitter)
    override var onMessage: ((PNMessageResult) -> Unit)? by emitterHelper::onMessage
    override var onPresence: ((PNPresenceEventResult) -> Unit)? by emitterHelper::onPresence
    override var onSignal: ((PNSignalResult) -> Unit)? by emitterHelper::onSignal
    override var onMessageAction: ((PNMessageActionResult) -> Unit)? by emitterHelper::onMessageAction
    override var onObjects: ((PNObjectEventResult) -> Unit)? by emitterHelper::onObjects
    override var onFile: ((PNFileEventResult) -> Unit)? by emitterHelper::onFile

    /**
     * Create a [BaseSubscriptionSet] that contains both subscriptions.
     *
     * @param subscription the other [BaseSubscription] to add to the [BaseSubscriptionSet]
     */
    override fun plus(subscription: Subscription): SubscriptionSet {
        return pubnub.subscriptionSetOf(setOf(this, subscription))
    }
}
