package com.pubnub.api.v2

import com.pubnub.api.models.consumer.pubsub.PNEvent
import com.pubnub.internal.v2.BaseChannelSubscriptionOptions
import com.pubnub.internal.v2.BaseSubscriptionOptions
import com.pubnub.internal.v2.Filter
import com.pubnub.internal.v2.ReceivePresenceEvents

interface SubscriptionOptions {
    operator fun plus(options: SubscriptionOptions): SubscriptionOptions
    fun allOptions(): Set<SubscriptionOptions>

    object Default : SubscriptionOptions by BaseSubscriptionOptions()
    object Channel : ChannelOptions by BaseChannelSubscriptionOptions()
}

interface ChannelOptions : SubscriptionOptions {
    override operator fun plus(options: SubscriptionOptions): ChannelOptions
}

fun SubscriptionOptions.filter(predicate: (PNEvent) -> Boolean): SubscriptionOptions =
    this + Filter(predicate)

fun ChannelOptions.receivePresenceEvents(): ChannelOptions =
    this + ReceivePresenceEvents(true)
