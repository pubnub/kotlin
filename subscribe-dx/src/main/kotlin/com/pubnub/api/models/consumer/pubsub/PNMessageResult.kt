package com.pubnub.api.models.consumer.pubsub

import com.google.gson.JsonElement
import com.pubnub.api.callbacks.SubscribeCallback

/**
 * Wrapper around an actual message received in [SubscribeCallback.message].
 */
data class PNMessageResult internal constructor(
    private val basePubSubResult: PubSubResult,
    override val message: JsonElement
) : MessageResult, PubSubResult by basePubSubResult
