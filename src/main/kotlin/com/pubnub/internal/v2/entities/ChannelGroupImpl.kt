package com.pubnub.internal.v2.entities

import com.pubnub.api.PubNub
import com.pubnub.api.subscribe.PRESENCE_CHANNEL_SUFFIX
import com.pubnub.api.v2.entities.ChannelGroup
import com.pubnub.api.v2.subscriptions.ChannelOptions
import com.pubnub.api.v2.subscriptions.SubscriptionOptions
import com.pubnub.internal.v2.subscription.ReceivePresenceEvents
import com.pubnub.internal.v2.subscription.SubscriptionImpl

internal class ChannelGroupImpl(private val pubNub: PubNub, val channelGroupName: ChannelGroupName) : ChannelGroup {

    override val name: String = channelGroupName.id

    override fun subscription(options: SubscriptionOptions): SubscriptionImpl {
        val channelOptions = options.allOptions.filterIsInstance<ChannelOptions>()
        val channelGroups = buildSet<ChannelGroupName> {
            add(channelGroupName)
            if (channelOptions.filterIsInstance<ReceivePresenceEvents>().isNotEmpty()) {
                add(channelGroupName.withPresence)
            }
        }
        return SubscriptionImpl(
            pubNub,
            channelGroups = channelGroups,
            options = options.filter { result ->
                return@filter channelGroups.any { it.id == result.subscription }
            }
        )
    }
}

@JvmInline
internal value class ChannelGroupName(val id: String)

internal val ChannelGroupName.withPresence get() = ChannelGroupName("${this.id}$PRESENCE_CHANNEL_SUFFIX")
internal val ChannelGroupName.isPresence get() = id.endsWith(PRESENCE_CHANNEL_SUFFIX)
