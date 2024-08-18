package com.pubnub.api.java.v2.callbacks

import com.pubnub.api.callbacks.Listener
import com.pubnub.api.java.PubNub
import com.pubnub.api.models.consumer.PNStatus

interface StatusListener : Listener {
    /**
     * Receive status updates from the PubNub client, such as:
     * * [com.pubnub.api.enums.PNStatusCategory.PNConnectedCategory],
     * * [com.pubnub.api.enums.PNStatusCategory.PNDisconnectedCategory],
     * * [com.pubnub.api.enums.PNStatusCategory.PNSubscriptionChanged]
     * * [com.pubnub.api.enums.PNStatusCategory.PNConnectionError],
     * * [com.pubnub.api.enums.PNStatusCategory.PNUnexpectedDisconnectCategory],
     *
     * @see [PNStatus]
     *
     * @param pubnub The client instance which has this listener attached.
     * @param status Wrapper around the actual message content.
     */
    fun status(pubnub: PubNub, status: PNStatus)
}
