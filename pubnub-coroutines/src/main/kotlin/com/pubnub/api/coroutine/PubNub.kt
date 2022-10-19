package com.pubnub.api.coroutine

import com.pubnub.api.Endpoint
import com.pubnub.api.PNConfiguration
import com.pubnub.api.models.consumer.PNStatus
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

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
        oldPubNub.publish(
            channel = channel,
            message = message,
            meta = meta,
            shouldStore = shouldStore,
            usePost = usePost,
            replicate = replicate,
            ttl = ttl
        ).coroutineResult().map { it.timetoken }

    suspend fun <Input, Output> Endpoint<Input, Output>.coroutineResult(): Result<Output> =
        suspendCoroutine { continuation ->
            val callback = { result: Output?, status: PNStatus ->
                if (status.error) continuation.resume(Result.failure(status.exception!!))
                else continuation.resume(Result.success(result!!))
            }
            async(callback)
        }
}
