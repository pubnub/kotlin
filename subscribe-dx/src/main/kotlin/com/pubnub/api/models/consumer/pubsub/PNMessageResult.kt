package com.pubnub.api.models.consumer.pubsub

import com.google.gson.JsonElement

/**
 * Wrapper around an actual message received in [SubscribeCallback.message].
 */
data class PNMessageResult(
    private val basePubSubResult: PubSubResult,
    override val message: JsonElement
) : MessageResult, PubSubResult by basePubSubResult
