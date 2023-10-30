package com.pubnub.api.v2.callbacks

import com.pubnub.api.PubNub
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.internal.BasePubNub
import com.pubnub.internal.v2.callbacks.StatusListener

abstract class StatusListener : StatusListener {

    final override fun status(pubnub: BasePubNub, status: PNStatus) {
        status(pubnub as PubNub, status)
    }

    /**
     * Receive status updates from the PubNub client.
     *
     * @see [PNStatus]
     *
     * @param pubnub The client instance which has this listener attached.
     * @param status Wrapper around the actual message content.
     */
    abstract fun status(pubnub: PubNub, status: PNStatus)
}
