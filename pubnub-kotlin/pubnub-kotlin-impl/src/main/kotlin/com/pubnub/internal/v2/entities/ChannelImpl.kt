package com.pubnub.internal.v2.entities

import com.pubnub.api.endpoints.files.DeleteFile
import com.pubnub.api.endpoints.files.SendFile
import com.pubnub.api.endpoints.pubsub.Publish
import com.pubnub.api.endpoints.pubsub.Signal
import com.pubnub.api.v2.callbacks.EventListener
import com.pubnub.api.v2.entities.Channel
import com.pubnub.api.v2.subscriptions.Subscription
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.v2.subscription.SubscriptionImpl
import java.io.InputStream

class ChannelImpl(pubnub: PubNubImpl, channelName: ChannelName) :
    BaseChannelImpl<EventListener, Subscription>(
        pubnub.pubNubCore,
        channelName,
        { channels, channelGroups, options -> SubscriptionImpl(pubnub, channels, channelGroups, options) },
    ),
    Channel {
    override fun publish(
        message: Any,
        meta: Any?,
        shouldStore: Boolean?,
        usePost: Boolean,
        replicate: Boolean,
        ttl: Int?
    ): Publish {
        return com.pubnub.internal.endpoints.pubsub.PublishImpl(
            pubnub.publish(
                channelName.id,
                message,
                meta,
                shouldStore,
                usePost,
                replicate,
                ttl,
            ),
        )
    }

    override fun signal(message: Any): Signal {
        return com.pubnub.internal.endpoints.pubsub.SignalImpl(pubnub.signal(channelName.id, message))
    }

    override fun fire(message: Any, meta: Any?, usePost: Boolean, ttl: Int?): Publish {
        return com.pubnub.internal.endpoints.pubsub.PublishImpl(
            pubnub.fire(
                channelName.id,
                message,
                meta,
                usePost,
                ttl,
            ),
        )
    }

    override fun sendFile(
        fileName: String,
        inputStream: InputStream,
        message: Any?,
        meta: Any?,
        ttl: Int?,
        shouldStore: Boolean?,
        cipherKey: String?
    ): SendFile {
        return com.pubnub.internal.endpoints.files.SendFileImpl(
            pubnub.sendFile(
                channelName.id,
                fileName,
                inputStream,
                message,
                meta,
                ttl,
                shouldStore,
                cipherKey,
            ),
        )
    }

    override fun deleteFile(
        fileName: String,
        fileId: String
    ): DeleteFile {
        return com.pubnub.internal.endpoints.files.DeleteFileImpl(
            pubnub.deleteFile(channelName.id, fileName, fileId),
        )
    }
}
