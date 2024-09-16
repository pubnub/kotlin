package com.pubnub.internal.java.v2.callbacks

import com.pubnub.api.PubNub
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.v2.callbacks.StatusListener

data class DelegatingStatusListener(
    private val listener: com.pubnub.api.java.v2.callbacks.StatusListener,
    private val pubnubJava: com.pubnub.api.java.PubNub,
) : StatusListener {
    override fun status(pubnub: PubNub, status: PNStatus) {
        listener.status(pubnubJava, status)
    }
}
