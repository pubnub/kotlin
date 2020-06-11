package com.pubnub.api.models.consumer.pubsub

import com.google.gson.JsonElement

class PNSignalResult(
    basePubSubResult: BasePubSubResult,
    messageResult: JsonElement
) : MessageResult(basePubSubResult, messageResult)