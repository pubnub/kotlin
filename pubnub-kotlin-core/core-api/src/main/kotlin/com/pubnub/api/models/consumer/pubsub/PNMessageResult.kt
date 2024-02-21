package com.pubnub.api.models.consumer.pubsub

import com.google.gson.JsonElement
import com.pubnub.api.PubNubError

/**
 * Wrapper around an actual message.
 */
data class PNMessageResult(
    private val basePubSubResult: PubSubResult,
    override val message: JsonElement,
    val error: PubNubError? = null
) : MessageResult, PubSubResult by basePubSubResult
