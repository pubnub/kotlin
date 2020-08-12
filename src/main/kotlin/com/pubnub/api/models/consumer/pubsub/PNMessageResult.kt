package com.pubnub.api.models.consumer.pubsub

import com.google.gson.JsonElement
import com.pubnub.api.callbacks.SubscribeCallback

/**
 * Wrapper around an actual message received in [SubscribeCallback.message].
 */
class PNMessageResult internal constructor(
    basePubSubResult: BasePubSubResult,
    messageResult: JsonElement
) : MessageResult(basePubSubResult, messageResult)
