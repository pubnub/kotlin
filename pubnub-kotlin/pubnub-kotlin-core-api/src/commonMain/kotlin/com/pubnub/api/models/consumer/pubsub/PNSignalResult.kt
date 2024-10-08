package com.pubnub.api.models.consumer.pubsub

import com.pubnub.api.JsonElement

/**
 * Wrapper around a received signal.
 */
data class PNSignalResult(
    private val basePubSubResult: PubSubResult,
    override val message: JsonElement,
) : MessageResult, PubSubResult by basePubSubResult
