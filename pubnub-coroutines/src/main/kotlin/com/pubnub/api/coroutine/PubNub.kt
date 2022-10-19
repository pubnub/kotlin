package com.pubnub.api.coroutine

import com.pubnub.api.PNConfiguration
import com.pubnub.api.endpoints.pubsub.PublishImpl

class PubNub(configuration: PNConfiguration) {

    private val oldPubNub = com.pubnub.api.PubNub(configuration)

    suspend fun publish(
        channel: String,
        message: Any,
        meta: Any? = null,
        shouldStore: Boolean? = null,
        usePost: Boolean = false,
        replicate: Boolean = true,
        ttl: Int? = null
    ): Result<Long> =
        PublishImpl(
            pubnub = oldPubNub,
            channel = channel,
            message = message,
            meta = meta,
            shouldStore = shouldStore,
            usePost = usePost,
            replicate = replicate,
            ttl = ttl
        ).justdoit().map { it.timetoken }
}
