package com.pubnub.internal.entities

import cocoapods.PubNubSwift.PubNubChannelMetadataEntityObjC
import cocoapods.PubNubSwift.PubNubSubscriptionObjC
import com.pubnub.api.v2.entities.ChannelMetadata
import com.pubnub.api.v2.subscriptions.Subscription
import com.pubnub.api.v2.subscriptions.SubscriptionOptions
import com.pubnub.internal.subscription.SubscriptionImpl
import kotlinx.cinterop.ExperimentalForeignApi

@OptIn(ExperimentalForeignApi::class)
class ChannelMetadataImpl(
    private val channelMetadata: PubNubChannelMetadataEntityObjC
) : ChannelMetadata {
    override val id: String
        get() = channelMetadata.name()

    override fun subscription(options: SubscriptionOptions): Subscription {
        // TODO: Add support for handling SubscriptionOptions
        return SubscriptionImpl(objCSubscription = PubNubSubscriptionObjC(entity = channelMetadata))
    }
}
