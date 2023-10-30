package com.pubnub.api.enums

import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.internal.BasePubNub.PubNubImpl
import com.pubnub.internal.callbacks.SubscribeCallback

/**
 * Check the status category via [PNStatus.category] in the `async` callback
 * when executing API methods like [PubNubImpl.publish] or [PubNubImpl.history].
 *
 * Or in the [SubscribeCallback.status] for API methods like [PubNubImpl.subscribe] or [PubNubImpl.unsubscribeAll] ie.
 * methods able to manage the channel mix.
 */
enum class PNStatusCategory {
    /**
     * Request failed because of access error (active PAM).
     * [PNStatus.affectedChannels] or [PNStatus.affectedChannelGroups] contain list of channels and groups
     * the client can't access to.
     */
    PNAccessDeniedCategory,

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
     * SDK was able to reconnect to PubNub, i.e. the subscription loop has been reconnected.
     */
    PNReconnectedCategory,

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
}
