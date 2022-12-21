package com.pubnub.api.models.consumer.pubsub

import com.google.gson.JsonElement
import com.pubnub.api.SpaceId
import com.pubnub.api.callbacks.SubscribeCallback
import com.pubnub.api.models.consumer.MessageType

/**
 * Wrapper around an actual message received in [SubscribeCallback.message].
 */
data class PNMessageResult internal constructor(
    private val basePubSubResult: PubSubResult,
    override val message: JsonElement,
    val spaceId: SpaceId?,
    val messageType: MessageType?
) : MessageResult, PubSubResult by basePubSubResult
