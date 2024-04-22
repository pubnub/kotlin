package com.pubnub.api.integration.hackathon

import com.pubnub.api.PubNub
import com.pubnub.api.models.consumer.presence.PNHereNowResult

// która będzie nasłuchiwałą i informowała o wybranej kontekscie konwersacji i wysyłała webhooka
// odpowiedzialność klasy
//
class ConversationContextMonitor(private val pubnubSupervisor: PubNub) {
    private fun getAllActiveChannels() {
        // Global HereNow is used. Remember about performance and security concerns.
        // get all channels from Presence. This is not the same as getting all channels from AppContext.
        val globalHereNowResult: PNHereNowResult? = pubnubSupervisor.hereNow().sync()
        val allChannels: MutableSet<String>? = globalHereNowResult?.channels?.keys
    }
}
