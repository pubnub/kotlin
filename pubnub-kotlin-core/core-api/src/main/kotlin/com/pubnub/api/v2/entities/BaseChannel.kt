package com.pubnub.api.v2.entities

import com.pubnub.api.v2.callbacks.BaseEventListener
import com.pubnub.api.v2.subscriptions.BaseSubscription
import com.pubnub.api.v2.subscriptions.SubscriptionOptions

/**
 * A representation of a PubNub channel identified by its [name].
 *
 * You can get a [BaseSubscription] to this channel through [subscription].
 *
 * Use the [com.pubnub.api.PubNub.channel] factory method to create instances of this interface.
 */
interface BaseChannel<Lis: BaseEventListener, Sub: BaseSubscription<Lis>> : Subscribable<Lis> {
    /**
     * The name of this channel. Supports wildcards by ending it with ".*"
     *
     * See more in the [documentation](https://www.pubnub.com/docs/general/channels/overview)
     */
    val name: String

    /**
     * Returns a [BaseSubscription] that can be used to subscribe to this channel.
     *
     * Channel subscriptions support passing [com.pubnub.api.v2.subscriptions.SubscriptionOptions.receivePresenceEvents] in
     * [options] to enable receiving presence events.
     *
     * [com.pubnub.api.v2.subscriptions.SubscriptionOptions.filter] can be used to filter events delivered to the subscription.
     *
     * For example, to create a subscription that only listens to presence events:
     * ```
     * channel.subscription(SubscriptionOptions.receivePresenceEvents() + SubscriptionOptions.filter { it is PNPresenceEventResult } )
     * ```
     *
     * @param options optional [SubscriptionOptions].
     * @return an inactive [BaseSubscription] to this channel. You must call [BaseSubscription.subscribe] to start receiving events.
     */
    override fun subscription(options: SubscriptionOptions): Sub
}
