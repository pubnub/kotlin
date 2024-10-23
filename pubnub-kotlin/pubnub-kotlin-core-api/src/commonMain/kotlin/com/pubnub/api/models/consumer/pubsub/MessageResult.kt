package com.pubnub.api.models.consumer.pubsub

import com.pubnub.api.JsonElement

/**
 * @property message The actual message content
 */
interface MessageResult : PubSubResult {
    val message: JsonElement
    val customMessageType: String?
}
