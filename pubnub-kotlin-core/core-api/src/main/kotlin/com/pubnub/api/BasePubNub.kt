package com.pubnub.api

import com.pubnub.api.v2.callbacks.BaseEventEmitter
import com.pubnub.api.v2.callbacks.BaseEventListener
import com.pubnub.api.v2.callbacks.BaseStatusEmitter
import com.pubnub.api.v2.callbacks.BaseStatusListener
import com.pubnub.api.v2.entities.BaseChannel
import com.pubnub.api.v2.entities.BaseChannelGroup
import com.pubnub.api.v2.entities.BaseChannelMetadata
import com.pubnub.api.v2.entities.BaseUserMetadata
import com.pubnub.api.v2.subscriptions.BaseSubscription
import com.pubnub.api.v2.subscriptions.BaseSubscriptionSet
import com.pubnub.api.v2.subscriptions.EmptyOptions
import com.pubnub.api.v2.subscriptions.SubscriptionOptions

interface BasePubNub<
        EventListener : BaseEventListener,
        Subscription : BaseSubscription<EventListener>,
        Channel : BaseChannel<EventListener, Subscription>,
        ChannelGroup : BaseChannelGroup<EventListener, Subscription>,
        ChannelMetadata: BaseChannelMetadata<EventListener, Subscription>,
        UserMetadata: BaseUserMetadata<EventListener, Subscription>,
        SubscriptionSet: BaseSubscriptionSet<EventListener, Subscription>,
        StatusListener: BaseStatusListener,
        > : BaseEventEmitter<EventListener>, BaseStatusEmitter<StatusListener> {

    fun channel(name: String): Channel
    fun channelGroup(name: String): ChannelGroup
    fun channelMetadata(id: String): ChannelMetadata
    fun userMetadata(id: String): UserMetadata
    fun subscriptionSetOf(subscriptions: Set<Subscription>): SubscriptionSet
    fun subscriptionSetOf(channels: Set<String> = emptySet(),
                          channelGroups: Set<String> = emptySet(),
                          options: SubscriptionOptions = EmptyOptions
    ): SubscriptionSet


    /**
     * Force the SDK to try and reach out PubNub. Monitor the results in [SubscribeCallback.status]
     *
     * @param timetoken optional timetoken to use for the subscription on reconnection.
     * Only applicable when [PNConfiguration.enableEventEngine] is true, otherwise ignored
     */
    fun reconnect(timetoken: Long = 0L)

    /**
     * Cancel any subscribe and heartbeat loops or ongoing re-connections.
     *
     * Monitor the results in [SubscribeCallback.status]
     */
    fun disconnect()

    /**
     * Frees up threads and allows for a clean exit.
     */
    fun destroy()

    /**
     * Same as [destroy] but immediately.
     */
    fun forceDestroy()
}