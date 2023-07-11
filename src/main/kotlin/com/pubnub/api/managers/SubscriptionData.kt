package com.pubnub.api.managers

class SubscriptionData {
    internal val channels: MutableSet<String> = mutableSetOf()
    internal val channelGroups: MutableSet<String> = mutableSetOf()
}
