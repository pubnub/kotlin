package com.pubnub.api.coroutine

class PubNub {

    suspend fun publish(
        channel: String,
        message: Any,
        meta: Any? = null,
        shouldStore: Boolean? = null,
        usePost: Boolean = false,
        replicate: Boolean = true,
        ttl: Int? = null
    ): Result<Long> {
        TODO()
    }
}
