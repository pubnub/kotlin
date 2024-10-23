package com.pubnub.internal.java.v2.entities

import com.pubnub.api.java.v2.endpoints.pubsub.PublishBuilder
import com.pubnub.api.java.v2.endpoints.pubsub.SignalBuilder
import com.pubnub.api.v2.subscriptions.EmptyOptions
import com.pubnub.api.v2.subscriptions.SubscriptionOptions
import com.pubnub.internal.java.PubNubForJavaImpl
import com.pubnub.internal.java.v2.subscription.SubscriptionImpl
import com.pubnub.internal.v2.entities.ChannelImpl
import com.pubnub.internal.v2.entities.ChannelName

class ChannelImpl(private val pubnub: PubNubForJavaImpl, channelName: ChannelName) :
    com.pubnub.api.java.v2.entities.Channel, ChannelImpl(pubnub, channelName) {
    override fun subscription(): com.pubnub.api.java.v2.subscriptions.Subscription {
        return subscription(EmptyOptions)
    }

    override fun subscription(options: SubscriptionOptions): SubscriptionImpl {
        return SubscriptionImpl.from(super.subscription(options))
    }

    override fun publish(message: Any): PublishBuilder {
        return pubnub.publish(message, name)
    }

    override fun signal(message: Any): SignalBuilder {
        return pubnub.signal(message, name)
    }

    override fun fire(message: Any): PublishBuilder {
        return pubnub.publish(message, name).replicate(false).shouldStore(false)
    }
}
