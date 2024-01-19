package com.pubnub.api.models.consumer.pubsub

import com.google.gson.JsonElement

/**
 * @property message The actual message content
 */
interface MessageResult : PubSubResult {
    val message: JsonElement
}
