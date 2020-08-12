package com.pubnub.api.models.consumer.pubsub

import com.google.gson.JsonElement

/**
 * @property message The actual message content
 */
open class MessageResult internal constructor(
    basePubSubResult: BasePubSubResult,
    val message: JsonElement
) : BasePubSubResult(basePubSubResult)
