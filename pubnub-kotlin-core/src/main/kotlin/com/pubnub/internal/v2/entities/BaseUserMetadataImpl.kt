package com.pubnub.internal.v2.entities

import com.pubnub.api.v2.entities.BaseUserMetadata
import com.pubnub.api.v2.subscriptions.SubscriptionOptions
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.SubscriptionFactory
import com.pubnub.internal.v2.subscription.BaseSubscriptionImpl

open class BaseUserMetadataImpl<T : BaseSubscriptionImpl>(internal val pubnub: PubNubImpl, val channelName: ChannelName, private val subscriptionFactory: SubscriptionFactory<T>) : BaseUserMetadata {

    override val id: String = channelName.id

    override fun subscription(options: SubscriptionOptions): T {
        val channels = setOf(channelName)
        return subscriptionFactory(
            pubnub,
            channels,
            emptySet(),
            SubscriptionOptions.filter { result ->
                // simple channel name or presence channel
                channels.any { it.id == result.channel }
            } + options
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is BaseUserMetadataImpl<*>) return false

        if (pubnub != other.pubnub) return false
        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        var result = pubnub.hashCode()
        result = 31 * result + id.hashCode()
        return result
    }
}
