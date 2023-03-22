package com.pubnub.api.models.consumer.pubsub

import com.google.gson.JsonElement
import com.pubnub.api.SpaceId
import com.pubnub.api.callbacks.SubscribeCallback

/**
 * Wrapper around a signal received in [SubscribeCallback.signal].
 */
data class PNSignalResult internal constructor(
    private val basePubSubResult: PubSubResult,
    override val message: JsonElement,
    override val spaceId: SpaceId?,
    override val type: String?
) : MessageResult, PubSubResult by basePubSubResult
