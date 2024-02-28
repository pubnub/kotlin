package com.pubnub.api.enums

enum class PNHeartbeatNotificationOptions {
    /**
     * No heartbeat-related events in [SubscribeCallback.status]
     */
    NONE,

    /**
     * Receive failed heartbeat events in [SubscribeCallback.status]
     */
    FAILURES,

    /**
     * Receive all heartbeat events in [SubscribeCallback.status]
     */
    ALL,
}
