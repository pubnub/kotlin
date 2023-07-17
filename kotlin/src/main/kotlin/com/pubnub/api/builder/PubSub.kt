package com.pubnub.api.builder

import com.pubnub.api.PubNub
import com.pubnub.api.managers.SubscriptionManager

object PubSub {
    /**
     * @see [PubNub.presence]
     */
    internal fun presence(
        subscriptionManager: SubscriptionManager,
        channels: List<String> = emptyList(),
        channelGroups: List<String> = emptyList(),
        connected: Boolean = false
    ) {
        val presenceOperation = PresenceOperation(
            connected = connected,
            channels = channels,
            channelGroups = channelGroups
        )
        subscriptionManager.adaptPresenceBuilder(presenceOperation)
    }

    /**
     * @see [PubNub.subscribe]
     */
    internal fun subscribe(
        subscriptionManager: SubscriptionManager,
        channels: List<String> = emptyList(),
        channelGroups: List<String> = emptyList(),
        withPresence: Boolean = false,
        withTimetoken: Long = 0L
    ) {

        val subscribeOperation = SubscribeOperation(
            channels = channels,
            channelGroups = channelGroups,
            presenceEnabled = withPresence,
            timetoken = withTimetoken
        )
        subscriptionManager.adaptSubscribeBuilder(subscribeOperation)
    }

    /**
     * @see [PubNub.unsubscribe]
     */
    internal fun unsubscribe(
        subscriptionManager: SubscriptionManager,
        channels: List<String> = emptyList(),
        channelGroups: List<String> = emptyList()
    ) {
        val unsubscribeOperation = UnsubscribeOperation(
            channels = channels,
            channelGroups = channelGroups
        )
        subscriptionManager.adaptUnsubscribeBuilder(unsubscribeOperation)
    }
}
