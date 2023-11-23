package com.pubnub.internal.v2

import com.pubnub.api.PubNub
import com.pubnub.api.subscribe.PRESENCE_CHANNEL_SUFFIX
import com.pubnub.api.v2.Channel
import com.pubnub.api.v2.ChannelOptions
import com.pubnub.api.v2.SubscriptionOptions
import com.pubnub.api.v2.filter

internal class ChannelImpl(private val pubNub: PubNub, val channelName: ChannelName) : Channel {

    override val name: String = channelName.id

    override fun subscription(options: SubscriptionOptions): SubscriptionImpl {
        val channelOptions = options.allOptions().filterIsInstance<ChannelOptions>()
        val channels = buildSet<ChannelName> {
            add(channelName)
            if (channelOptions.filterIsInstance<ReceivePresenceEvents>()
                .firstOrNull()?.receiveEvents == true
            ) {
                add(channelName.withPresence)
            }
        }
        return SubscriptionImpl(
            pubNub, channels = channels,
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
