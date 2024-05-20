package com.pubnub.api.models.consumer.pubsub

import com.pubnub.api.JsonValue
import com.pubnub.api.PubNubError

/**
 * Wrapper around an actual message.
 */
class PNMessageResult(
    private val basePubSubResult: PubSubResult,
    override val message: JsonValue,
    val error: PubNubError? = null,
) : MessageResult, PubSubResult by basePubSubResult {
    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other == null) {
            return false
        }
        if (this::class != other::class) {
            return false
        }

        other as PNMessageResult

        if (basePubSubResult != other.basePubSubResult) {
            return false
        }
        if (message != other.message) {
            return false
        }
        if (error != other.error) {
            return false
        }

        return true
    }

    override fun hashCode(): Int {
        var result = basePubSubResult.hashCode()
        result = 31 * result + message.hashCode()
        result = 31 * result + (error?.hashCode() ?: 0)
        return result
    }
}
