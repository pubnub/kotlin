package com.pubnub.api.subscribe

import com.pubnub.api.subscribe.internal.SubscribeModule

internal class Subscribe(private val subscribeModule: SubscribeModule) {

    fun subscribe(
        channels: List<String>,
        channelGroups: List<String>,
        withPresence: Boolean,
        withTimetoken: Long
    ) {
        subscribeModule.handleEvent(
            Commands.SubscribeIssued(
                channels = channels,
                groups = channelGroups
            )
        )
    }

    fun unsubscribe(
        channels: List<String> = emptyList(),
        channelGroups: List<String> = emptyList()
    ) {
        subscribeModule.handleEvent(
            Commands.UnsubscribeIssued(
                channels = channels,
                groups = channelGroups
            )
        )
    }

    fun unsubscribeAll() {
        subscribeModule.handleEvent(
            Commands.UnsubscribeAllIssued
        )
    }

    fun getSubscribedChannels(): Collection<String> = subscribeModule.status().channels

    fun getSubscribedChannelGroups(): Collection<String> = subscribeModule.status().channels
}
