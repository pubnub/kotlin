package com.pubnub.internal.v2.entities

import com.pubnub.api.PubNub
import com.pubnub.api.subscribe.PRESENCE_CHANNEL_SUFFIX
import com.pubnub.api.v2.entities.Channel
import com.pubnub.api.v2.subscriptions.ChannelOptions
import com.pubnub.api.v2.subscriptions.SubscriptionOptions
import com.pubnub.internal.v2.subscription.ReceivePresenceEvents
import com.pubnub.internal.v2.subscription.SubscriptionImpl

internal class ChannelImpl(internal val pubnub: PubNub, val channelName: ChannelName) : Channel {

    override val name: String = channelName.id

    override fun subscription(options: SubscriptionOptions): SubscriptionImpl {
        val channelOptions = options.allOptions.filterIsInstance<ChannelOptions>()
        val channels = buildSet<ChannelName> {
            add(channelName)
            if (channelOptions.filterIsInstance<ReceivePresenceEvents>().isNotEmpty()) {
                add(channelName.withPresence)
            }
        }
        return SubscriptionImpl(
            pubnub,
            channels = channels,
            options = options.filter { result ->
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
            }
        )
    }
}

@JvmInline
internal value class ChannelName(val id: String)

internal val ChannelName.withPresence get() = ChannelName("${this.id}$PRESENCE_CHANNEL_SUFFIX")
internal val ChannelName.isPresence get() = id.endsWith(PRESENCE_CHANNEL_SUFFIX)
