package com.pubnub.api.models.consumer.pubsub

import com.google.gson.JsonElement
import com.pubnub.api.callbacks.SubscribeCallback

/**
 * Wrapper around a signal received in [SubscribeCallback.signal].
 */
class PNSignalResult(
    basePubSubResult: BasePubSubResult,
    messageResult: JsonElement
) : MessageResult(basePubSubResult, messageResult)
