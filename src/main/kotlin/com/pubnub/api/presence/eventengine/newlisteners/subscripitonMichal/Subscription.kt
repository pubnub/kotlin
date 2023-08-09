package com.pubnub.api.presence.eventengine.newlisteners.subscripitonMichal

import java.util.function.Consumer

interface Subscription {
    var onMessage: Consumer<String> //replace String with PNMessage
    fun cancel()
    fun pauseOnMessage()
    fun resumeOnMessage()
}
