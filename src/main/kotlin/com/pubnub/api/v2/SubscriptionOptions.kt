package com.pubnub.api.v2

import com.pubnub.api.models.consumer.pubsub.PNEvent
import com.pubnub.internal.v2.Filter
import com.pubnub.internal.v2.ReceivePresenceEvents

open class SubscriptionOptions internal constructor(
    optionsSet: Set<SubscriptionOptions> = emptySet()
) {
    val allOptions = optionsSet.toSet()
        get() = field.ifEmpty {
            setOf(this)
        }

    open operator fun plus(options: SubscriptionOptions): SubscriptionOptions {
        val newOptions = buildSet {
            addAll(allOptions)
            addAll(options.allOptions)
        }
        return SubscriptionOptions(newOptions)
    }

    fun filter(predicate: (PNEvent) -> Boolean): SubscriptionOptions =
        this + Filter(predicate)

    object Default : SubscriptionOptions()

    companion object {
        fun filter(predicate: (PNEvent) -> Boolean): SubscriptionOptions = Filter(predicate)
    }
}

open class ChannelOptions internal constructor(
    optionsSet: Set<SubscriptionOptions> = emptySet()
) : SubscriptionOptions(optionsSet) {
    override operator fun plus(options: SubscriptionOptions): ChannelOptions {
        return ChannelOptions(super.plus(options).allOptions)
    }

    object Default : ChannelOptions()

    fun receivePresenceEvents(): ChannelOptions =
        this + ReceivePresenceEvents()

    companion object {
        fun receivePresenceEvents(): ChannelOptions = ReceivePresenceEvents()
    }
}
