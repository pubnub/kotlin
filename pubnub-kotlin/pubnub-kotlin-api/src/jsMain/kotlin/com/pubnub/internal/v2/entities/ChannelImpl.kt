package com.pubnub.internal.v2.entities

import PubNub
import com.pubnub.api.endpoints.files.DeleteFile
import com.pubnub.api.endpoints.files.SendFile
import com.pubnub.api.endpoints.pubsub.Publish
import com.pubnub.api.endpoints.pubsub.Signal
import com.pubnub.api.v2.entities.Channel
import com.pubnub.api.v2.subscriptions.ReceivePresenceEventsImpl
import com.pubnub.api.v2.subscriptions.Subscription
import com.pubnub.api.v2.subscriptions.SubscriptionOptions
import com.pubnub.internal.v2.subscriptions.SubscriptionImpl
import com.pubnub.kmp.Uploadable
import com.pubnub.kmp.createJsObject

class ChannelImpl( private val jsChannel: dynamic): Channel {

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
        get() = jsChannel.name

    override fun subscription(options: SubscriptionOptions): Subscription { //todo use options
        return SubscriptionImpl(jsChannel.subscription(createJsObject<PubNub.SubscriptionOptions> {
            if (options.allOptions.filterIsInstance<ReceivePresenceEventsImpl>().isNotEmpty()) {
                receivePresenceEvents = true
            }
        }))
    }

}