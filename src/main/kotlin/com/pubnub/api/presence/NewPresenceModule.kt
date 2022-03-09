package com.pubnub.api.presence

import com.pubnub.api.PubNub
import com.pubnub.api.presence.internal.InternalPresenceModule

interface NewPresenceModule {
    companion object {
        fun create(
            pubnub: PubNub
        ): NewPresenceModule {
            return InternalPresenceModule.create(pubnub = pubnub)
        }
    }

    fun presence(
        channels: List<String> = emptyList(),
        channelGroups: List<String> = emptyList(),
        connected: Boolean = false
    )

    fun unsubscribeAll()

    fun cancel()
}
