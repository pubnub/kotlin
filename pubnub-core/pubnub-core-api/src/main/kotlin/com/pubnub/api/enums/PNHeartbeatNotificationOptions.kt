package com.pubnub.api.enums

enum class PNHeartbeatNotificationOptions {
    /**
     * Do not report heartbeat events through [com.pubnub.api.v2.callbacks.StatusListener]
     */
    NONE,

    /**
     * Receive failed heartbeat events in [com.pubnub.api.v2.callbacks.StatusListener]
     */
    FAILURES,

    /**
     * Receive all heartbeat events in [com.pubnub.api.v2.callbacks.StatusListener]
     */
    ALL,
}
