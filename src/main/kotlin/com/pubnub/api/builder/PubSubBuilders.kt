package com.pubnub.api.builder

import com.pubnub.api.managers.SubscriptionManager

abstract class PubSubBuilder(
    protected val subscriptionManager: SubscriptionManager,
    var channels: List<String> = emptyList(),
    var channelGroups: List<String> = emptyList()
) {
    abstract fun execute()
}

class PresenceBuilder(
    subscriptionManager: SubscriptionManager,
    channels: List<String> = emptyList(),
    channelGroups: List<String> = emptyList(),
    var connected: Boolean = false
) : PubSubBuilder(subscriptionManager, channels, channelGroups) {

    override fun execute() {
        val presenceOperation = PresenceOperation().apply {
            connected = this@PresenceBuilder.connected
            channels = this@PresenceBuilder.channels
            channelGroups = this@PresenceBuilder.channelGroups
        }
        subscriptionManager.adaptPresenceBuilder(presenceOperation)
    }
}

class SubscribeBuilder(
    subscriptionManager: SubscriptionManager,
    var withPresence: Boolean = false,
    var withTimetoken: Long = 0L
) : PubSubBuilder(subscriptionManager) {

    override fun execute() {
        val subscribeOperation = SubscribeOperation().apply {
            channels = this@SubscribeBuilder.channels
            channelGroups = this@SubscribeBuilder.channelGroups
            presenceEnabled = this@SubscribeBuilder.withPresence
            timetoken = this@SubscribeBuilder.withTimetoken
        }
        this.subscriptionManager.adaptSubscribeBuilder(subscribeOperation)
    }
}

class UnsubscribeBuilder(
    subscriptionManager: SubscriptionManager
) : PubSubBuilder(subscriptionManager) {

    override fun execute() {
        val unsubscribeOperation = UnsubscribeOperation().apply {
            channels = this@UnsubscribeBuilder.channels
            channelGroups = this@UnsubscribeBuilder.channelGroups
        }
        this.subscriptionManager.adaptUnsubscribeBuilder(unsubscribeOperation)
    }
}
