package com.pubnub.internal.callbacks

import com.pubnub.api.BasePubNub
import com.pubnub.api.PubNub
import com.pubnub.api.callbacks.SubscribeCallback
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.internal.v2.callbacks.DelegatingEventListener
import com.pubnub.internal.v2.callbacks.InternalStatusListener

data class DelegatingSubscribeCallback(private val listener: SubscribeCallback) :
    com.pubnub.internal.callbacks.SubscribeCallback,
    DelegatingEventListener(
        listener,
    ),
    InternalStatusListener {
    override fun status(
        pubnub: BasePubNub<*, *, *, *, *, *, *, *>,
        status: PNStatus,
    ) {
        listener.status(pubnub as PubNub, status)
    }
}
