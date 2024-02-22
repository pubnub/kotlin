package com.pubnub.internal.v2.callbacks

import com.pubnub.api.BasePubNub
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.v2.callbacks.BaseStatusListener

interface InternalStatusListener : BaseStatusListener {
    /**
     * Receive status updates from the PubNub client.
     *
     * @see [PNStatus]
     *
     * @param pubnub The client instance which has this listener attached.
     * @param status Wrapper around the actual message content.
     */
    fun status(pubnub: BasePubNub<*,*,*,*,*,*,*,*>, status: PNStatus)
}
