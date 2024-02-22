package com.pubnub.api

import com.pubnub.api.callbacks.SubscribeCallback
import com.pubnub.api.v2.callbacks.EventListener
import com.pubnub.api.v2.callbacks.StatusListener
import com.pubnub.api.v2.entities.Channel
import com.pubnub.api.v2.entities.ChannelGroup
import com.pubnub.api.v2.entities.ChannelMetadata
import com.pubnub.api.v2.entities.UserMetadata
import com.pubnub.api.v2.subscription.Subscription
import com.pubnub.api.v2.subscription.SubscriptionSet
import com.pubnub.internal.PubNubImpl

interface PubNub : BasePubNub<EventListener, Subscription, Channel, ChannelGroup, ChannelMetadata, UserMetadata, SubscriptionSet, StatusListener> {
    companion object {
        @JvmStatic
        fun create(configuration: PNConfiguration): PubNub {
            return PubNubImpl(configuration)
        }
    }

    fun addListener(listener: SubscribeCallback)
}