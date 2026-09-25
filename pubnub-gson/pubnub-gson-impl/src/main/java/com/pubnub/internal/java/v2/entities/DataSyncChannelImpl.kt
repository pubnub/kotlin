package com.pubnub.internal.java.v2.entities

import com.pubnub.api.java.v2.subscriptions.Subscription
import com.pubnub.api.v2.subscriptions.EmptyOptions
import com.pubnub.api.v2.subscriptions.SubscriptionOptions
import com.pubnub.internal.java.PubNubForJavaImpl
import com.pubnub.internal.java.v2.subscription.SubscriptionImpl

class DataSyncChannelImpl(pubnub: PubNubForJavaImpl, id: String) :
    com.pubnub.internal.v2.entities.DataSyncChannelImpl(pubnub, id),
    com.pubnub.api.java.v2.entities.DataSyncChannel {
    override fun subscription(options: SubscriptionOptions): SubscriptionImpl {
        return SubscriptionImpl.from(super<com.pubnub.internal.v2.entities.DataSyncChannelImpl>.subscription(options))
    }

    override fun subscription(): Subscription {
        return subscription(EmptyOptions)
    }

    override fun subscription(projection: String, options: SubscriptionOptions): SubscriptionImpl {
        return SubscriptionImpl.from(
            super<com.pubnub.internal.v2.entities.DataSyncChannelImpl>.subscription(projection, options)
        )
    }

    override fun subscription(projection: String): Subscription {
        return subscription(projection, EmptyOptions)
    }
}
