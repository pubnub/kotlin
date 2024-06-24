package com.pubnub.internal.entities

import cocoapods.PubNubSwift.PubNubChannelEntityObjC
import cocoapods.PubNubSwift.PubNubSubscriptionObjC
import com.pubnub.api.endpoints.files.DeleteFile
import com.pubnub.api.endpoints.files.SendFile
import com.pubnub.api.endpoints.pubsub.Publish
import com.pubnub.api.endpoints.pubsub.Signal
import com.pubnub.api.v2.entities.Channel
import com.pubnub.api.v2.subscriptions.Subscription
import com.pubnub.api.v2.subscriptions.SubscriptionOptions
import com.pubnub.internal.subscription.SubscriptionImpl
import com.pubnub.kmp.Uploadable
import kotlinx.cinterop.ExperimentalForeignApi

@OptIn(ExperimentalForeignApi::class)
class ChannelImpl(
    private val channel: PubNubChannelEntityObjC
): Channel {
    override fun publish(
        message: Any,
        meta: Any?,
        shouldStore: Boolean?,
        usePost: Boolean,
        replicate: Boolean,
        ttl: Int?
    ): Publish {
        TODO("Not yet implemented")
    }

    override fun signal(message: Any): Signal {
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
        cipherKey: String?
    ): SendFile {
        TODO("Not yet implemented")
    }

    override fun deleteFile(fileName: String, fileId: String): DeleteFile {
        TODO("Not yet implemented")
    }

    override val name: String
        get() = channel.name()

    override fun subscription(options: SubscriptionOptions): Subscription {
        // TODO: Add support for handling SubscriptionOptions
        return SubscriptionImpl(objCSubscription = PubNubSubscriptionObjC(entity = channel))
    }
}