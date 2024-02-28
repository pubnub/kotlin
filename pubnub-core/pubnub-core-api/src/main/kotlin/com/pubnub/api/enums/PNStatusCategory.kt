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
    Connected,
    SubscriptionChanged,
    UnexpectedDisconnect,
    Disconnected,
    ConnectionError,
    HeartbeatFailed,
    HeartbeatSuccess,
    MalformedMessage,
}
