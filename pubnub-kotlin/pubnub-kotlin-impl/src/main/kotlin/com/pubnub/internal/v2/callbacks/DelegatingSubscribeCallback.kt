package com.pubnub.internal.v2.callbacks

import com.pubnub.api.BasePubNub
import com.pubnub.api.PubNub
import com.pubnub.api.callbacks.SubscribeCallback
import com.pubnub.api.models.consumer.PNStatus

data class DelegatingSubscribeCallback(private val listener: SubscribeCallback) :
    DelegatingEventListener(listener),
    com.pubnub.internal.callbacks.SubscribeCallback {
    override fun status(
        pubnub: BasePubNub<*, *, *, *, *, *, *, *>,
        status: PNStatus,
    ) {
        listener.status(pubnub as PubNub, status)
    }
}
