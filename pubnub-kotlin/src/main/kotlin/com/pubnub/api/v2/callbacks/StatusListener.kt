package com.pubnub.api.v2.callbacks

import com.pubnub.api.PubNub
import com.pubnub.api.models.consumer.PNStatus

interface StatusListener : BaseStatusListener {
    /**
     * Receive status updates from the PubNub client.
     *
     * @see [PNStatus]
     *
     * @param pubnub The client instance which has this listener attached.
     * @param status Wrapper around the actual message content.
     */
    fun status(pubnub: PubNub, status: PNStatus)
}
