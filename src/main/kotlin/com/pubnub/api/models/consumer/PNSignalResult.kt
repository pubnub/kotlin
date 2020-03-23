package com.pubnub.api.models.consumer

import com.google.gson.JsonElement
import com.pubnub.api.models.consumer.pubsub.BasePubSubResult
import com.pubnub.api.models.consumer.pubsub.MessageResult

class PNSignalResult(
    basePubSubResult: BasePubSubResult,
    message: JsonElement
) : MessageResult(basePubSubResult, message)