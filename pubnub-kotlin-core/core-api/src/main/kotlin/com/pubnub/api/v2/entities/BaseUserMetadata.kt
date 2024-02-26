package com.pubnub.api.v2.entities

import com.pubnub.api.v2.callbacks.BaseEventListener
import com.pubnub.api.v2.subscriptions.BaseSubscription
import com.pubnub.api.v2.subscriptions.SubscriptionOptions

interface BaseUserMetadata<Lis : BaseEventListener, Sub : BaseSubscription<Lis>> : Subscribable<Lis> {
    val id: String

    override fun subscription(options: SubscriptionOptions): Sub
}
