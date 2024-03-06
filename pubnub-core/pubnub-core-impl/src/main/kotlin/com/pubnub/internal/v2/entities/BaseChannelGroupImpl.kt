package com.pubnub.internal.v2.entities

import com.pubnub.api.v2.callbacks.BaseEventListener
import com.pubnub.api.v2.entities.BaseChannelGroup
import com.pubnub.api.v2.subscriptions.BaseSubscription
import com.pubnub.api.v2.subscriptions.ReceivePresenceEventsImpl
import com.pubnub.api.v2.subscriptions.SubscriptionOptions
import com.pubnub.internal.PubNubCore
import com.pubnub.internal.SubscriptionFactory
import com.pubnub.internal.subscribe.PRESENCE_CHANNEL_SUFFIX

open class BaseChannelGroupImpl<Lis : BaseEventListener, Sub : BaseSubscription<Lis>>(
    internal val pubNub: PubNubCore,
    private val channelGroupName: ChannelGroupName,
    private val subscriptionFactory: SubscriptionFactory<Sub>,
) : BaseChannelGroup<Lis, Sub> {
    override val name: String = channelGroupName.id

    override fun subscription(options: SubscriptionOptions): Sub {
        val channelGroups =
            buildSet {
                add(channelGroupName)
                if (options.allOptions.filterIsInstance<ReceivePresenceEventsImpl>().isNotEmpty()) {
                    add(channelGroupName.withPresence)
                }
            }
        return subscriptionFactory(
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
        if (other !is BaseChannelGroupImpl<*, *>) {
            return false
        }

        if (pubNub != other.pubNub) {
            return false
        }
        if (name != other.name) {
            return false
        }

        return true
    }

    override fun hashCode(): Int {
        var result = pubNub.hashCode()
        result = 31 * result + name.hashCode()
        return result
    }
}

@JvmInline
value class ChannelGroupName(val id: String) {
    val withPresence get() = ChannelGroupName("${this.id}$PRESENCE_CHANNEL_SUFFIX")
    val isPresence get() = id.endsWith(PRESENCE_CHANNEL_SUFFIX)
}
