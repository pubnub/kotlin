package com.pubnub.api.v2.callbacks

import com.pubnub.api.PubNub
import com.pubnub.api.models.consumer.PNStatus

abstract class StatusListener {
    /**
     * Receive messages at subscribed channels.
     *
     * @see [PubNub.subscribe]
     *
     * @param pubnub The client instance which has this listener attached.
     * @param status Wrapper around the actual message content.
     */
    abstract fun status(pubnub: PubNub, status: PNStatus)
}
