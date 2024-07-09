package com.pubnub.internal.entities

import cocoapods.PubNubSwift.PubNubChannelGroupEntityObjC
import cocoapods.PubNubSwift.PubNubSubscriptionObjC
import com.pubnub.api.v2.entities.ChannelGroup
import com.pubnub.api.v2.subscriptions.Subscription
import com.pubnub.api.v2.subscriptions.SubscriptionOptions
import com.pubnub.internal.subscription.SubscriptionImpl
import kotlinx.cinterop.ExperimentalForeignApi

@OptIn(ExperimentalForeignApi::class)
class ChannelGroupImpl(
    private val channelGroup: PubNubChannelGroupEntityObjC
) : ChannelGroup {
    override val name: String
        get() = channelGroup.name()

    override fun subscription(options: SubscriptionOptions): Subscription {
        // TODO: Add support for handling SubscriptionOptions
        return SubscriptionImpl(objCSubscription = PubNubSubscriptionObjC(entity = channelGroup))
    }
}
