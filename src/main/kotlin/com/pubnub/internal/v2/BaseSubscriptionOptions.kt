package com.pubnub.internal.v2

import com.pubnub.api.models.consumer.pubsub.PNEvent
import com.pubnub.api.v2.ChannelOptions
import com.pubnub.api.v2.SubscriptionOptions

internal open class BaseSubscriptionOptions internal constructor() : SubscriptionOptions {
    override operator fun plus(options: SubscriptionOptions): SubscriptionOptions {
        val newOptions = buildSet {
            addAll(allOptions())
            addAll(options.allOptions())
        }
        return SubscriptionOptionsSet(newOptions)
    }

    override fun allOptions(): Set<SubscriptionOptions> = setOf(this)
}

internal class SubscriptionOptionsSet(
    internal val options: Set<SubscriptionOptions> = emptySet()
) : BaseSubscriptionOptions() {
    override fun allOptions(): Set<SubscriptionOptions> = options
}

internal class ChannelSubscriptionOptionsSet(
    internal val options: Set<SubscriptionOptions> = emptySet()
) : BaseChannelSubscriptionOptions() {
    override fun allOptions(): Set<SubscriptionOptions> = options
}

internal open class BaseChannelSubscriptionOptions : BaseSubscriptionOptions(), ChannelOptions {
    override operator fun plus(options: SubscriptionOptions): BaseChannelSubscriptionOptions {
        return ChannelSubscriptionOptionsSet(super.plus(options).allOptions())
    }
}

internal class Filter(val predicate: (PNEvent) -> Boolean) : BaseSubscriptionOptions()

internal class ReceivePresenceEvents(val receiveEvents: Boolean) : BaseChannelSubscriptionOptions()
