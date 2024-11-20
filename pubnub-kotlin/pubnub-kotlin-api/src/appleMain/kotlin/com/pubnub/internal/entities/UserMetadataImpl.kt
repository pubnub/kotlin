package com.pubnub.internal.entities

import cocoapods.PubNubSwift.KMPSubscription
import cocoapods.PubNubSwift.KMPUserMetadataEntity
import com.pubnub.api.v2.entities.UserMetadata
import com.pubnub.api.v2.subscriptions.Subscription
import com.pubnub.api.v2.subscriptions.SubscriptionOptions
import com.pubnub.internal.subscription.SubscriptionImpl
import kotlinx.cinterop.ExperimentalForeignApi

@OptIn(ExperimentalForeignApi::class)
class UserMetadataImpl(
    private val userMetadata: KMPUserMetadataEntity
) : UserMetadata {
    override val id: String
        get() = userMetadata.name()

    override fun subscription(options: SubscriptionOptions): Subscription {
        // TODO: Add support for handling SubscriptionOptions
        return SubscriptionImpl(objCSubscription = KMPSubscription(entity = userMetadata))
    }
}
