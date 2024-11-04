package com.pubnub.internal.entities

import cocoapods.PubNubSwift.KMPChannelEntity
import cocoapods.PubNubSwift.KMPSubscription
import com.pubnub.api.endpoints.files.DeleteFile
import com.pubnub.api.endpoints.files.SendFile
import com.pubnub.api.endpoints.pubsub.Publish
import com.pubnub.api.endpoints.pubsub.Signal
import com.pubnub.api.v2.entities.Channel
import com.pubnub.api.v2.subscriptions.ReceivePresenceEventsImpl
import com.pubnub.api.v2.subscriptions.Subscription
import com.pubnub.api.v2.subscriptions.SubscriptionOptions
import com.pubnub.internal.subscription.SubscriptionImpl
import com.pubnub.kmp.Uploadable
import kotlinx.cinterop.ExperimentalForeignApi

@OptIn(ExperimentalForeignApi::class)
class ChannelImpl(
    private val channel: KMPChannelEntity
) : Channel {
    override fun publish(
        message: Any,
        meta: Any?,
        shouldStore: Boolean,
        usePost: Boolean,
        replicate: Boolean,
        ttl: Int?,
        customMessageType: String?
    ): Publish {
        TODO("Not yet implemented")
    }

    override fun signal(message: Any, customMessageType: String?): Signal {
        TODO("Not yet implemented")
    }

    override fun fire(message: Any, meta: Any?, usePost: Boolean, ttl: Int?): Publish {
        TODO("Not yet implemented")
    }

    override fun sendFile(
        fileName: String,
        inputStream: Uploadable,
        message: Any?,
        meta: Any?,
        ttl: Int?,
        shouldStore: Boolean?,
        cipherKey: String?,
        customMessageType: String?
    ): SendFile {
        TODO("Not yet implemented")
    }

    override fun deleteFile(fileName: String, fileId: String): DeleteFile {
        TODO("Not yet implemented")
    }

    override val name: String
        get() = channel.name()

    // TODO: Add support for handling SubscriptionOptions
    override fun subscription(options: SubscriptionOptions): Subscription {
        val presenceOptions = options.allOptions.filterIsInstance<ReceivePresenceEventsImpl>()
        val objcSubscription = KMPSubscription(channel, presenceOptions.isNotEmpty())

        return SubscriptionImpl(objcSubscription)
    }
}
