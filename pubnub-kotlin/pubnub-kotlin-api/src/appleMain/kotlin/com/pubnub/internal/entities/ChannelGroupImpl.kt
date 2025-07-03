package com.pubnub.internal.entities

import cocoapods.PubNubSwift.KMPChannelGroupEntity
import cocoapods.PubNubSwift.KMPSubscription
import com.pubnub.api.v2.entities.ChannelGroup
import com.pubnub.api.v2.subscriptions.ReceivePresenceEventsImpl
import com.pubnub.api.v2.subscriptions.Subscription
import com.pubnub.api.v2.subscriptions.SubscriptionOptions
import com.pubnub.internal.subscription.SubscriptionImpl
import kotlinx.cinterop.ExperimentalForeignApi

@OptIn(ExperimentalForeignApi::class)
class ChannelGroupImpl(
    private val channelGroup: KMPChannelGroupEntity
) : ChannelGroup {
    override val name: String
        get() = channelGroup.name()

    override fun subscription(options: SubscriptionOptions): Subscription {
        val presenceOptions = options.allOptions.filterIsInstance<ReceivePresenceEventsImpl>()
        val objcSubscription = KMPSubscription(channelGroup, presenceOptions.isNotEmpty())

        return SubscriptionImpl(objcSubscription)
    }
}
