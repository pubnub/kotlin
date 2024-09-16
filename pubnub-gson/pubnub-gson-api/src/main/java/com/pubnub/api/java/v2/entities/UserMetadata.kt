package com.pubnub.api.java.v2.entities

import com.pubnub.api.java.v2.subscriptions.Subscription
import com.pubnub.api.v2.entities.Subscribable
import com.pubnub.api.v2.subscriptions.SubscriptionOptions

/**
 * A representation of a PubNub entity for tracking user metadata changes.
 *
 * You can get a [Subscription] to listen for metadata events through [Subscribable.subscription].
 *
 * Use the [com.pubnub.api.java.PubNub.userMetadata] factory method to create instances of this interface.
 */
interface UserMetadata : com.pubnub.api.java.v2.entities.Subscribable {
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
    override fun subscription(options: SubscriptionOptions): Subscription

    /**
     * Returns a [Subscription] that can be used to subscribe to this channel.
     *
     * The returned [Subscription] is initially inactive. You must call [Subscription.subscribe] on it
     * to start receiving events.
     *
     * @return An inactive [Subscription] to this channel.
     */
    fun subscription(): Subscription
}
