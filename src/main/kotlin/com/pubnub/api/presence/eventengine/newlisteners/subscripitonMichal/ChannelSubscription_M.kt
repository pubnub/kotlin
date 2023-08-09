package com.pubnub.api.presence.eventengine.newlisteners.subscripitonMichal

import java.util.function.Consumer

class ChannelSubscription_M(
    val channel: String,
    override var onMessage: (String) -> Unit,
    withPresence: Boolean = false,
    private val onCancel: (ChannelSubscription_M) -> Unit,
) : Subscription {
    internal var shouldHandleMessage = true

    override fun cancel() {
        onCancel(this)
    }

    //todo shouldn't we provide a way to check if onmessage is paused/resumed ? That would be too much.
    override fun pauseOnMessage() {
        shouldHandleMessage = false
    }

    override fun resumeOnMessage() {
        shouldHandleMessage = true
    }
}
