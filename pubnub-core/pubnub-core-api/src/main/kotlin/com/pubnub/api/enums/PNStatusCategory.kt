package com.pubnub.api.enums

import com.pubnub.api.models.consumer.PNStatus

/**
 * Check the status category via [PNStatus.category] in the `async` callback
 * when executing API methods like [PubNub.publish] or [PubNub.history].
 *
 * Or in the [SubscribeCallback.status] for API methods like [PubNub.subscribe] or [PubNub.unsubscribeAll] i.e.
 * methods able to manage the channel mix.
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
     * Previously started subscribe loop did fail and at this moment client disconnected from real-time data channels.
     */
    PNUnexpectedDisconnectCategory,
    /**
     * The subscription has been stopped.
     */
    PNDisconnectedCategory,
    /**
     * Previously started subscribe loop failed, and at this moment client disconnected from real-time data channels.
     */
    PNConnectionError,
    PNHeartbeatFailed,
    PNHeartbeatSuccess,
    /**
     * PubNub sent a malformed response.
     * This may happen when you connect to a public WiFi Hotspot that requires you to auth via your web browser first,
     * or if there is a proxy somewhere returning an HTML access denied error,
     * or if there was an intermittent server issue.
     */
    PNMalformedResponseCategory,
}
