package com.pubnub.api.v2.entities

import com.pubnub.api.v2.callbacks.BaseEventListener
import com.pubnub.api.v2.subscriptions.BaseSubscription
import com.pubnub.api.v2.subscriptions.SubscriptionOptions

interface BaseUserMetadata<Lis : BaseEventListener, Sub : BaseSubscription<Lis>> : Subscribable<Lis> {
    /**
     * The id for this user metadata object.
     *
     * See more in the [documentation](https://www.pubnub.com/docs/general/metadata/users-metadata)
     */
    val id: String

    /**
     * Returns a [Subscription] that can be used to subscribe to this user metadata.
     *
     * [com.pubnub.api.v2.subscriptions.SubscriptionOptions.filter] can be used to filter events delivered to the subscription.
     *
     * @param options optional [SubscriptionOptions].
     * @return an inactive [Subscription] to this user metadata. You must call [Subscription.subscribe] to start receiving events.
     */
    override fun subscription(options: SubscriptionOptions): Sub
}
