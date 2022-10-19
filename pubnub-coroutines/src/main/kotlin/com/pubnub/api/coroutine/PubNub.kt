package com.pubnub.api.coroutine

import com.pubnub.api.models.consumer.PNPublishResult

class PubNub {

    suspend fun publish(
        channel: String,
        message: Any,
        meta: Any? = null,
        shouldStore: Boolean? = null,
        usePost: Boolean = false,
        replicate: Boolean = true,
        ttl: Int? = null
    ): PNPublishResult {
        TODO()
    }
}
