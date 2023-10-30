package com.pubnub.internal.v2.callbacks

import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.internal.BasePubNub
import com.pubnub.internal.callbacks.Listener

interface StatusListener : Listener {
    /**
     * Receive status updates from the PubNub client.
     *
     * @see [PNStatus]
     *
     * @param pubnub The client instance which has this listener attached.
     * @param status Wrapper around the actual message content.
     */
    fun status(pubnub: BasePubNub, status: PNStatus)
}
