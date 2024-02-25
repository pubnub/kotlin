package com.pubnub.internal.callbacks

import com.pubnub.api.BasePubNub
import com.pubnub.api.PubNub
import com.pubnub.api.callbacks.SubscribeCallback
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.internal.v2.callbacks.DelegatingEventListener
import com.pubnub.internal.v2.callbacks.InternalStatusListener

class DelegatingSubscribeCallback(private val listener: SubscribeCallback) : com.pubnub.internal.callbacks.SubscribeCallback, DelegatingEventListener(listener), InternalStatusListener {

    override fun status(pubnub: BasePubNub<*, *, *, *, *, *, *, *>, status: PNStatus) {
        listener.status(pubnub as PubNub, status)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other || this.listener === other) return true
        if (other !is DelegatingSubscribeCallback) return false

        if (listener != other.listener) return false

        return true
    }

    override fun hashCode(): Int {
        return listener.hashCode()
    }
}
