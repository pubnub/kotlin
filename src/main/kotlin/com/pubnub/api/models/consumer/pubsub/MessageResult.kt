package com.pubnub.api.models.consumer.pubsub

import com.google.gson.JsonElement
import com.pubnub.api.SpaceId

/**
 * @property message The actual message content
 */
interface MessageResult : PubSubResult {
    val message: JsonElement
    val spaceId: SpaceId?
    val type: String?
}
