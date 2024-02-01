package com.pubnub.internal.v2.entities

import com.pubnub.api.PubNub
import com.pubnub.api.subscribe.PRESENCE_CHANNEL_SUFFIX
import com.pubnub.api.v2.entities.Channel
import com.pubnub.api.v2.subscriptions.Filter
import com.pubnub.api.v2.subscriptions.SubscriptionOptions
import com.pubnub.internal.v2.subscription.ReceivePresenceEventsImpl
import com.pubnub.internal.v2.subscription.SubscriptionImpl

internal class ChannelImpl(internal val pubnub: PubNub, private val channelName: ChannelName) : Channel {

    override val name: String = channelName.id

    override fun subscription(options: SubscriptionOptions?): SubscriptionImpl {
        val channels = buildSet<ChannelName> {
            add(channelName)
            if (options?.allOptions?.filterIsInstance<ReceivePresenceEventsImpl>()?.isNotEmpty() == true) {
                add(channelName.withPresence)
            }
        }
        return SubscriptionImpl(
            pubnub,
            channels = channels,
            options = Filter { result ->
                // simple channel name or presence channel
                if (channels.any { it.id == result.channel }) {
                    return@Filter true
                }

                // wildcard channels
                if (name.endsWith(".*") &&
                    (
                            result.subscription == name ||
                                    result.channel.startsWith(name.substringBeforeLast("*"))
                            )
                ) {
                    return@Filter true
                }
                return@Filter false
            } + options
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ChannelImpl

        if (pubnub != other.pubnub) return false
        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        var result = pubnub.hashCode()
        result = 31 * result + name.hashCode()
        return result
    }


}

@JvmInline
internal value class ChannelName(val id: String) {
    val withPresence get() = ChannelName("${this.id}$PRESENCE_CHANNEL_SUFFIX")
    val isPresence get() = id.endsWith(PRESENCE_CHANNEL_SUFFIX)
}