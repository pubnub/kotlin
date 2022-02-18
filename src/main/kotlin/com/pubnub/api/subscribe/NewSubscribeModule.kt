package com.pubnub.api.subscribe

import com.pubnub.api.subscribe.internal.Commands
import com.pubnub.api.subscribe.internal.SubscribeModuleInternals

internal class NewSubscribeModule(private val internals: SubscribeModuleInternals) {

    fun subscribe(
        channels: List<String>,
        channelGroups: List<String>,
        withPresence: Boolean,
        withTimetoken: Long
    ) {
        internals.handleEvent(
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
        internals.handleEvent(
            Commands.UnsubscribeIssued(
                channels = channels,
                groups = channelGroups
            )
        )
    }

    fun unsubscribeAll() {
        internals.handleEvent(
            Commands.UnsubscribeAllIssued
        )
    }

    fun getSubscribedChannels(): List<String> = internals.status().channels.toList()

    fun getSubscribedChannelGroups(): List<String> = internals.status().channels.toList()
}
