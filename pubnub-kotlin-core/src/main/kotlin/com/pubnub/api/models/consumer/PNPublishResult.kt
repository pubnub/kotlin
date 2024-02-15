package com.pubnub.api.models.consumer

/**
 * Result of the Publish operation
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
