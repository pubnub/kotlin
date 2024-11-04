package com.pubnub.internal.v2.entities

import com.pubnub.api.endpoints.files.DeleteFile
import com.pubnub.api.endpoints.files.SendFile
import com.pubnub.api.endpoints.pubsub.Publish
import com.pubnub.api.endpoints.pubsub.Signal
import com.pubnub.api.v2.entities.Channel
import com.pubnub.api.v2.subscriptions.ReceivePresenceEventsImpl
import com.pubnub.api.v2.subscriptions.SubscriptionOptions
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.subscribe.PRESENCE_CHANNEL_SUFFIX
import com.pubnub.internal.v2.subscription.SubscriptionImpl
import java.io.InputStream

open class ChannelImpl(val pubNubImpl: PubNubImpl, val channelName: ChannelName) : Channel {
    override val name: String = channelName.id

    override fun subscription(options: SubscriptionOptions): SubscriptionImpl {
        val channels =
            buildSet<ChannelName> {
                add(channelName)
                if (options.allOptions.filterIsInstance<ReceivePresenceEventsImpl>().isNotEmpty()) {
                    add(channelName.withPresence)
                }
            }
        return SubscriptionImpl(
            pubNubImpl,
            channels,
            emptySet(),
            SubscriptionOptions.filter { result ->
                // simple channel name or presence channel
                if (channels.any { it.id == result.channel }) {
                    return@filter true
                }

                // wildcard channels
                if (name.endsWith(".*") &&
                    (
                        result.subscription == name ||
                            result.channel.startsWith(name.substringBeforeLast("*"))
                    )
                ) {
                    return@filter true
                }
                return@filter false
            } + options,
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other !is ChannelImpl) {
            return false
        }

        if (pubNubImpl != other.pubNubImpl) {
            return false
        }
        if (name != other.name) {
            return false
        }

        return true
    }

    override fun hashCode(): Int {
        var result = pubNubImpl.hashCode()
        result = 31 * result + name.hashCode()
        return result
    }

    override fun publish(
        message: Any,
        meta: Any?,
        shouldStore: Boolean,
        usePost: Boolean,
        replicate: Boolean,
        ttl: Int?,
        customMessageType: String?
    ): Publish {
        return pubNubImpl.publish(channelName.id, message, meta, shouldStore, usePost, replicate, ttl, customMessageType)
    }

    override fun signal(message: Any, customMessageType: String?): Signal {
        return pubNubImpl.signal(channelName.id, message, customMessageType)
    }

    override fun fire(message: Any, meta: Any?, usePost: Boolean, ttl: Int?): Publish {
        return pubNubImpl.fire(channelName.id, message, meta, usePost, ttl)
    }

    override fun sendFile(
        fileName: String,
        inputStream: InputStream,
        message: Any?,
        meta: Any?,
        ttl: Int?,
        shouldStore: Boolean?,
        cipherKey: String?,
        customMessageType: String?
    ): SendFile {
        return pubNubImpl.sendFile(channelName.id, fileName, inputStream, message, meta, ttl, shouldStore, cipherKey, customMessageType)
    }

    override fun deleteFile(
        fileName: String,
        fileId: String
    ): DeleteFile {
        return pubNubImpl.deleteFile(channelName.id, fileName, fileId)
    }
}

@JvmInline
value class ChannelName(val id: String) {
    val withPresence get() = ChannelName("${this.id}$PRESENCE_CHANNEL_SUFFIX")
    val isPresence get() = id.endsWith(PRESENCE_CHANNEL_SUFFIX)
}
