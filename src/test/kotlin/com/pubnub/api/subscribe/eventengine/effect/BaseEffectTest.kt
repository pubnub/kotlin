package com.pubnub.api.subscribe.eventengine.effect

import com.google.gson.JsonPrimitive
import com.pubnub.api.models.consumer.pubsub.BasePubSubResult
import com.pubnub.api.models.consumer.pubsub.PNEvent
import com.pubnub.api.models.consumer.pubsub.PNMessageResult

abstract class BaseEffectTest {
    protected fun createPNMessageResultList(message: String = "Test"): List<PNEvent> {
        val basePubSubResult =
            BasePubSubResult(
                "channel1",
                "my.*",
                16832048617009353L,
                null,
                "client-c2804687-7d25-4f0b-a442-e3820265b46c"
            )
        val pnMessageResult = PNMessageResult(basePubSubResult, JsonPrimitive(message))
        return listOf(pnMessageResult)
    }
}
