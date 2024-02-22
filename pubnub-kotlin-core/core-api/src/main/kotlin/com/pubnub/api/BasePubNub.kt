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
    fun subscriptionSetOf(channels: Set<String>,
                          channelGroups: Set<String>,
                          options: SubscriptionOptions
    ): SubscriptionSet
}