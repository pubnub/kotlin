package com.pubnub.internal.v2.entities

import com.pubnub.api.v2.callbacks.EventListener
import com.pubnub.api.v2.endpoints.pubsub.PublishBuilder
import com.pubnub.api.v2.endpoints.pubsub.SignalBuilder
import com.pubnub.api.v2.entities.Channel
import com.pubnub.api.v2.subscriptions.EmptyOptions
import com.pubnub.api.v2.subscriptions.Subscription
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.endpoints.pubsub.PublishImpl
import com.pubnub.internal.endpoints.pubsub.SignalImpl
import com.pubnub.internal.v2.subscription.SubscriptionImpl

class ChannelImpl(pubnub: PubNubImpl, channelName: String) :
    BaseChannelImpl<EventListener, Subscription>(
        pubnub.pubNubCore,
        ChannelName(channelName),
        { channels, channelGroups, options -> SubscriptionImpl(pubnub, channels, channelGroups, options) },
    ),
    Channel {
    override fun subscription(): Subscription {
        return subscription(EmptyOptions)
    }

    override fun publish(message: Any): PublishBuilder {
        return PublishImpl(pubnub, message, channelName.id)
    }

    override fun signal(message: Any): SignalBuilder {
        return SignalImpl(pubnub, message, channelName.id)
    }

    override fun fire(message: Any): PublishBuilder {
        return PublishImpl(pubnub, message, channelName.id)
    }
}
