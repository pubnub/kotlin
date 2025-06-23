package com.pubnub.api.enums

import com.pubnub.api.models.consumer.PNStatus

/**
 * Check the status category via [PNStatus.category] in the [com.pubnub.api.v2.callbacks.StatusListener] added to a
 * [com.pubnub.api.PubNub] object.
 */
enum class PNStatusCategory {
    /**
     * SDK successfully connected the Subscribe loop.
     */
    PNConnectedCategory,

    /**
     * SDK subscribed with a new mix of channels (fired every time the channel / channel group mix changed) since the
     * initial connection.
     */
    PNSubscriptionChanged,

    /**
     * Previously started subscribe loop failed and at this moment client is disconnected from real-time data channels.
     */
    PNUnexpectedDisconnectCategory,

    /**
     * The subscription has been stopped as requested (when all channels and channel groups have been unsubscribed).
     */
    PNDisconnectedCategory,

    /**
     * The subscription loop was not able to connect, and at this moment the client is disconnected from real-time
     * data channels.
     */
    PNConnectionError,

    /**
     * A background implicit Heartbeat request attempt failed.
     */
    PNHeartbeatFailed,

    /**
     * A background implicit Heartbeat request was successful.
     */
    PNHeartbeatSuccess,
}
