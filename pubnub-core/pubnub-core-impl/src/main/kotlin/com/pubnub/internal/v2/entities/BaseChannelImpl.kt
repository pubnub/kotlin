package com.pubnub.internal.v2.entities

import com.pubnub.api.v2.callbacks.BaseEventListener
import com.pubnub.api.v2.entities.BaseChannel
import com.pubnub.api.v2.subscriptions.BaseSubscription
import com.pubnub.api.v2.subscriptions.ReceivePresenceEventsImpl
import com.pubnub.api.v2.subscriptions.SubscriptionOptions
import com.pubnub.internal.PubNubCore
import com.pubnub.internal.SubscriptionFactory
import com.pubnub.internal.subscribe.PRESENCE_CHANNEL_SUFFIX

open class BaseChannelImpl<Lis : BaseEventListener, Sub : BaseSubscription<Lis>>(
    protected val pubnub: PubNubCore,
    protected val channelName: ChannelName,
    private val subscriptionFactory: SubscriptionFactory<Sub>,
) : BaseChannel<Lis, Sub> {
    override val name: String = channelName.id

    override fun subscription(options: SubscriptionOptions): Sub {
        val channels =
            buildSet<ChannelName> {
                add(channelName)
                if (options.allOptions.filterIsInstance<ReceivePresenceEventsImpl>().isNotEmpty()) {
                    add(channelName.withPresence)
                }
            }
        return subscriptionFactory(
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
        if (other !is BaseChannelImpl<*, *>) {
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
value class ChannelName(val id: String) {
    val withPresence get() = ChannelName("${this.id}$PRESENCE_CHANNEL_SUFFIX")
    val isPresence get() = id.endsWith(PRESENCE_CHANNEL_SUFFIX)
}
