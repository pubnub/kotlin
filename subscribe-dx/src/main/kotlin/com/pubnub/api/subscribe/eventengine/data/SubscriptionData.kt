package com.pubnub.api.subscribe.eventengine.data

class SubscriptionData {
    val channels: MutableSet<String> = mutableSetOf()
    val channelGroups: MutableSet<String> = mutableSetOf()
}
