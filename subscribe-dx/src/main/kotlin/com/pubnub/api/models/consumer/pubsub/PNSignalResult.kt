package com.pubnub.api.models.consumer.pubsub

import com.google.gson.JsonElement
import com.pubnub.api.callbacks.SubscribeCallback

/**
 * Wrapper around a signal received in [SubscribeCallback.signal].
 */
data class PNSignalResult internal constructor(
    private val basePubSubResult: PubSubResult,
    override val message: JsonElement
) : MessageResult, PubSubResult by basePubSubResult
