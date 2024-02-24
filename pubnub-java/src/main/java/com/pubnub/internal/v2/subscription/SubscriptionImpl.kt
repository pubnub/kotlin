package com.pubnub.internal.v2.subscription

import com.pubnub.api.v2.callbacks.EventListener
import com.pubnub.api.v2.subscriptions.Subscription
import com.pubnub.api.v2.subscriptions.SubscriptionSet
import com.pubnub.api.v2.subscriptions.BaseSubscription
import com.pubnub.api.v2.subscriptions.BaseSubscriptionSet
import com.pubnub.api.v2.subscriptions.SubscriptionOptions
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.v2.callbacks.DelegatingEventListener
import com.pubnub.internal.v2.entities.ChannelGroupName
import com.pubnub.internal.v2.entities.ChannelName

class SubscriptionImpl(
    pubnub: PubNubImpl,
    channels: Set<ChannelName>,
    channelGroups: Set<ChannelGroupName>,
    options: SubscriptionOptions
) : Subscription, BaseSubscriptionImpl<EventListener>(pubnub.internalPubNubClient, channels, channelGroups, options) {
    /**
     * Add a listener.
     *
     * @param listener The listener to be added.
     */
    override fun addListener(listener: EventListener) {
        addListener(DelegatingEventListener(listener))
    }

    /**
     * Create a [BaseSubscriptionSet] that contains both subscriptions.
     *
     * @param subscription the other [BaseSubscription] to add to the [BaseSubscriptionSet]
     */
    override fun plus(subscription: Subscription): SubscriptionSet {
        TODO("Not yet implemented")
    }
}