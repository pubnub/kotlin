package com.pubnub.internal.subscribe.eventengine.data

internal class SubscriptionData {
    internal val channels: MutableSet<String> = mutableSetOf()
    internal val channelGroups: MutableSet<String> = mutableSetOf()
}
