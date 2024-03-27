package com.pubnub.api.v2.entities

import com.pubnub.api.v2.callbacks.EventListener
import com.pubnub.api.v2.subscriptions.Subscription

/**
 * A representation of a PubNub entity for tracking channel metadata changes.
 *
 * You can get a [Subscription] to listen for metadata events through [Subscribable.subscription].
 *
 * Use the [com.pubnub.api.PubNub.channelMetadata] factory method to create instances of this interface.
 */
interface ChannelMetadata : BaseChannelMetadata<EventListener, Subscription> {
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
