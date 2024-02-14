package com.pubnub.api.models.consumer

import com.pubnub.internal.PubNubImpl

/**
 * Result of the [PubNubImpl.publish] operation
 *
 * @property timetoken The time token when the message or signal was published.
 */
class PNPublishResult internal constructor(
    val timetoken: Long
) {
    override fun toString(): String {
        return "PNPublishResult(timetoken=$timetoken)"
    }
}
