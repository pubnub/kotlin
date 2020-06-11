package com.pubnub.api.models.consumer.pubsub

import com.google.gson.JsonElement

class PNMessageResult(
    basePubSubResult: BasePubSubResult,
    messageResult: JsonElement
) : MessageResult(basePubSubResult, messageResult)