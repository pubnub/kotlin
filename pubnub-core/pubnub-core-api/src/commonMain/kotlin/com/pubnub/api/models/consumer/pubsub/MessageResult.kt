package com.pubnub.api.models.consumer.pubsub

import com.pubnub.api.JsonValue

/**
 * @property message The actual message content
 */
interface MessageResult : PubSubResult {
    val message: JsonValue
}
