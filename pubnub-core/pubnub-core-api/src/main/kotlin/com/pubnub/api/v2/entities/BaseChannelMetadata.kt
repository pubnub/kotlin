package com.pubnub.api.v2.entities

import com.pubnub.api.v2.callbacks.BaseEventListener
import com.pubnub.api.v2.subscriptions.BaseSubscription
import com.pubnub.api.v2.subscriptions.SubscriptionOptions

interface BaseChannelMetadata<Lis : BaseEventListener, Sub : BaseSubscription<Lis>> : Subscribable<Lis> {
    /**
     * The id for this channel metadata object.
     *
     * See more in the [documentation](https://www.pubnub.com/docs/general/metadata/channel-metadata)
     */
    val id: String

    /**
     * Returns a [Subscription] that can be used to subscribe to this channel metadata.
     *
     * [com.pubnub.api.v2.subscriptions.SubscriptionOptions.filter] can be used to filter events delivered to the subscription.
     *
     * @param options optional [SubscriptionOptions].
     * @return an inactive [Subscription] to this channel metadata. You must call [Subscription.subscribe] to start receiving events.
     */
    override fun subscription(options: SubscriptionOptions): Sub
}
