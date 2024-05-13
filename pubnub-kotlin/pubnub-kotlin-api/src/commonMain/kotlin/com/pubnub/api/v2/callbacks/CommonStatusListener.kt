package com.pubnub.api.v2.callbacks

import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.kmp.CommonPubNub

/**
 * Implement this interface and pass it into [com.pubnub.api.v2.callbacks.StatusEmitter.addListener] to listen for
 * PubNub connection status changes.
 */
interface CommonStatusListener : BaseStatusListener {
    /**
     * Receive status updates from the PubNub client, such as:
     * * [PNStatusCategory.PNConnectedCategory],
     * * [PNStatusCategory.PNDisconnectedCategory],
     * * [PNStatusCategory.PNSubscriptionChanged]
     * * [PNStatusCategory.PNConnectionError],
     * * [PNStatusCategory.PNUnexpectedDisconnectCategory],
     * * [PNStatusCategory.PNAcknowledgmentCategory]
     *
     * @see [PNStatus]
     *
     * @param pubnub The client instance which has this listener attached.
     * @param status Wrapper around the actual message content.
     */
    fun status(
        pubnub: CommonPubNub,
        status: PNStatus,
    )
}