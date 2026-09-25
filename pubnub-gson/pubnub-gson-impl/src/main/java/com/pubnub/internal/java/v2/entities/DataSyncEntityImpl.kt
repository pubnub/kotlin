package com.pubnub.internal.java.v2.entities

import com.pubnub.api.java.v2.subscriptions.Subscription
import com.pubnub.api.v2.subscriptions.EmptyOptions
import com.pubnub.api.v2.subscriptions.SubscriptionOptions
import com.pubnub.internal.java.PubNubForJavaImpl
import com.pubnub.internal.java.v2.subscription.SubscriptionImpl

class DataSyncEntityImpl(pubnub: PubNubForJavaImpl, id: String) :
    com.pubnub.internal.v2.entities.DataSyncEntityImpl(pubnub, id),
    com.pubnub.api.java.v2.entities.DataSyncEntity {
    override fun subscription(options: SubscriptionOptions): SubscriptionImpl {
        return SubscriptionImpl.from(super<com.pubnub.internal.v2.entities.DataSyncEntityImpl>.subscription(options))
    }

    override fun subscription(): Subscription {
        return subscription(EmptyOptions)
    }

    override fun subscription(projection: String, options: SubscriptionOptions): SubscriptionImpl {
        return SubscriptionImpl.from(
            super<com.pubnub.internal.v2.entities.DataSyncEntityImpl>.subscription(projection, options)
        )
    }

    override fun subscription(projection: String): Subscription {
        return subscription(projection, EmptyOptions)
    }
}
