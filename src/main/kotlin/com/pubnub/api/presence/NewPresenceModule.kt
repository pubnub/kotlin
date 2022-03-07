package com.pubnub.api.presence

import com.pubnub.api.presence.internal.Commands
import com.pubnub.api.presence.internal.PresenceModuleInternals

internal class NewPresenceModule(private val internals: PresenceModuleInternals) {

    fun presence(
        channels: List<String> = emptyList(),
        channelGroups: List<String> = emptyList(),
        connected: Boolean = false
    ) {
        if (connected) {
            internals.handleEvent(Commands.SubscribeIssued(channels = channels, groups = channelGroups))
        } else {
            internals.handleEvent(Commands.UnsubscribeIssued(channels = channels, groups = channelGroups))
        }
    }

    fun unsubscribeAll() {
        internals.handleEvent(Commands.UnsubscribeAllIssued)
    }
}
