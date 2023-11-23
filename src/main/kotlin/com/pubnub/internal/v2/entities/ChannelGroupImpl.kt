package com.pubnub.internal.v2.entities

import com.pubnub.api.PubNub
import com.pubnub.api.subscribe.PRESENCE_CHANNEL_SUFFIX
import com.pubnub.api.v2.entities.ChannelGroup
import com.pubnub.api.v2.subscriptions.SubscriptionOptions
import com.pubnub.internal.v2.subscription.ReceivePresenceEventsImpl
import com.pubnub.internal.v2.subscription.SubscriptionImpl

internal class ChannelGroupImpl(internal val pubNub: PubNub, private val channelGroupName: ChannelGroupName) : ChannelGroup {

    override val name: String = channelGroupName.id

    override fun subscription(options: SubscriptionOptions): SubscriptionImpl {
        val channelGroups = buildSet<ChannelGroupName> {
            add(channelGroupName)
            if (options.allOptions.filterIsInstance<ReceivePresenceEventsImpl>().isNotEmpty()) {
                add(channelGroupName.withPresence)
            }
        }
        return SubscriptionImpl(
            pubNub,
            channelGroups = channelGroups,
            options = SubscriptionOptions.filter { result ->
                channelGroups.any { it.id == result.subscription }
            } + options
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ChannelGroupImpl

        if (pubNub != other.pubNub) return false
        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        var result = pubNub.hashCode()
        result = 31 * result + name.hashCode()
        return result
    }
}

@JvmInline
internal value class ChannelGroupName(val id: String) {
    val withPresence get() = ChannelGroupName("${this.id}$PRESENCE_CHANNEL_SUFFIX")
    val isPresence get() = id.endsWith(PRESENCE_CHANNEL_SUFFIX)
}
