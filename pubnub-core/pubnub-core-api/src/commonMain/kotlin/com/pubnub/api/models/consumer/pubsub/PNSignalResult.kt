package com.pubnub.api.models.consumer.pubsub

import com.pubnub.api.JsonValue

/**
 * Wrapper around a received signal.
 */
data class PNSignalResult(
    private val basePubSubResult: PubSubResult,
    override val message: JsonValue,
) : MessageResult, PubSubResult by basePubSubResult
