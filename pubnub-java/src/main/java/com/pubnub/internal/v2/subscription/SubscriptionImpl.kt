package com.pubnub.internal.v2.subscription

import com.pubnub.api.v2.callbacks.EventListener
import com.pubnub.api.v2.subscription.Subscription
import com.pubnub.api.v2.subscription.SubscriptionSet
import com.pubnub.api.v2.subscriptions.BaseSubscription
import com.pubnub.api.v2.subscriptions.BaseSubscriptionSet
import com.pubnub.api.v2.subscriptions.SubscriptionOptions
import com.pubnub.internal.InternalPubNubClient
import com.pubnub.internal.v2.callbacks.DelegatingEventListener
import com.pubnub.internal.v2.entities.ChannelGroupName
import com.pubnub.internal.v2.entities.ChannelName

class SubscriptionImpl(
    pubnub: InternalPubNubClient,
    channels: Set<ChannelName>,
    channelGroups: Set<ChannelGroupName>,
    options: SubscriptionOptions
) : Subscription, BaseSubscriptionImpl<EventListener>(pubnub, channels, channelGroups, options) {
    /**
     * Add a listener.
     *
     * @param listener The listener to be added.
     */
    override fun addListener(listener: EventListener) {
        addListener(DelegatingEventListener(listener))
    }
//
//    private val emitterHelper = EmitterHelper(eventEmitter)
//    override var onMessage: ((PNMessageResult) -> Unit)? by emitterHelper::onMessage
//    override var onPresence: ((PNPresenceEventResult) -> Unit)? by emitterHelper::onPresence
//    override var onSignal: ((PNSignalResult) -> Unit)? by emitterHelper::onSignal
//    override var onMessageAction: ((PNMessageActionResult) -> Unit)? by emitterHelper::onMessageAction
//    override var onObjects: ((PNObjectEventResult) -> Unit)? by emitterHelper::onObjects
//    override var onFile: ((PNFileEventResult) -> Unit)? by emitterHelper::onFile

    /**
     * Create a [BaseSubscriptionSet] that contains both subscriptions.
     *
     * @param subscription the other [BaseSubscription] to add to the [BaseSubscriptionSet]
     */
    override fun plus(subscription: Subscription): SubscriptionSet {
        TODO("Not yet implemented")
    }
}