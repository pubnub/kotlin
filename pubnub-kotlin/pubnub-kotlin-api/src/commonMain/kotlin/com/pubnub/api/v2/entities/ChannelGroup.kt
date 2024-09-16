package com.pubnub.api.v2.entities

import com.pubnub.api.v2.subscriptions.Subscription
import com.pubnub.api.v2.subscriptions.SubscriptionOptions

/**
 * A representation of a PubNub channel group identified by its [name].
 *
 * You can get a [Subscription] to this channel group through [Subscribable.subscription].
 *
 * Use the [com.pubnub.api.PubNub.channelGroup] factory method to create instances of this interface.
 */
interface ChannelGroup : Subscribable {
    /**
     * The name of this channel group.
     *
     * See more in the [documentation](https://www.pubnub.com/docs/general/channels/subscribe#channel-groups)
     */
    val name: String

    /**
     * Returns a [Subscription] that can be used to subscribe to this channel group.
     *
     * Channel group subscriptions support passing [com.pubnub.api.v2.subscriptions.SubscriptionOptions.receivePresenceEvents]
     * in [options] to enable receiving presence events.
     *
     * [com.pubnub.api.v2.subscriptions.SubscriptionOptions.filter] can be used to filter events delivered to the subscription.
     *
     * For example, to create a subscription that only listens to presence events:
     * ```
     * channelGroup.subscription(SubscriptionOptions.receivePresenceEvents() + SubscriptionOptions.filter { it is PNPresenceEventResult } )
     * ```
     *
     * *Warning:* if a channel is part of more than one channel group, and you create subscription to both (or more)
     * those groups using a single [com.pubnub.api.PubNub] instance, you will only receive events for that channel in
     * one channel group subscription.
     *
     * For example, let's say "channel_1" is part of groups "cg_1" and "cg_2". If you only subscribe to "cg_1",
     * or you only subscribe to "cg_2", you will get all events for "channel_1". However, if in your app you subscribe
     * to both "cg_1" and "cg_2" at the same time, you will only receive events for "channel_1" in one of those
     * subscriptions, chosen at random.
     *
     * This limitation is due to how the server manages channels and channel groups.
     *
     * @param options optional [SubscriptionOptions].
     * @return an inactive [Subscription] to this channel group. You must call [Subscription.subscribe] to start receiving events.
     */
    override fun subscription(options: SubscriptionOptions): Subscription
}
