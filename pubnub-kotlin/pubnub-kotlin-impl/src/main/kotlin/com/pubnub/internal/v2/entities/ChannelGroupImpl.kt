package com.pubnub.internal.v2.entities

import com.pubnub.api.v2.entities.ChannelGroup
import com.pubnub.api.v2.subscriptions.ReceivePresenceEventsImpl
import com.pubnub.api.v2.subscriptions.SubscriptionOptions
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.subscribe.PRESENCE_CHANNEL_SUFFIX
import com.pubnub.internal.v2.subscription.SubscriptionImpl

open class ChannelGroupImpl(val pubnub: PubNubImpl, val channelGroupName: ChannelGroupName) : ChannelGroup {
    override val name: String = channelGroupName.id

    override fun subscription(options: SubscriptionOptions): SubscriptionImpl {
        val channelGroups =
            buildSet {
                add(channelGroupName)
                if (options.allOptions.filterIsInstance<ReceivePresenceEventsImpl>().isNotEmpty()) {
                    add(channelGroupName.withPresence)
                }
            }
        return SubscriptionImpl(
            pubnub,
            emptySet(),
            channelGroups,
            SubscriptionOptions.filter { result ->
                channelGroups.any { it.id == result.subscription }
            } + options,
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other !is ChannelGroupImpl) {
            return false
        }

        if (pubnub != other.pubnub) {
            return false
        }
        if (name != other.name) {
            return false
        }

        return true
    }

    override fun hashCode(): Int {
        var result = pubnub.hashCode()
        result = 31 * result + name.hashCode()
        return result
    }
}

@JvmInline
value class ChannelGroupName(val id: String) {
    val withPresence get() = ChannelGroupName("${this.id}$PRESENCE_CHANNEL_SUFFIX")
    val isPresence get() = id.endsWith(PRESENCE_CHANNEL_SUFFIX)
}
