package com.pubnub.internal.callbacks

import com.pubnub.api.BasePubNub
import com.pubnub.api.PubNub
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.v2.callbacks.StatusListener

data class DelegatingStatusListener(private val listener: StatusListener) :
    com.pubnub.internal.v2.callbacks.StatusListenerCore {
    override fun status(
        pubnub: BasePubNub<*, *, *, *, *, *, *, *>,
        status: PNStatus,
    ) {
        listener.status(pubnub as PubNub, status)
    }
}
