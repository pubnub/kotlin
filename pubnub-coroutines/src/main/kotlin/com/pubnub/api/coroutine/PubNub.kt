package com.pubnub.api.coroutine

import com.pubnub.api.Endpoint
import com.pubnub.api.PNConfiguration
import com.pubnub.api.models.consumer.PNStatus
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.jvm.isAccessible

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
        ).suspended().map { it.timetoken }


    private inline fun <reified T> T.callPrivateFunc(name: String, vararg args: Any?): Any? =
        T::class
            .declaredMemberFunctions
            .firstOrNull { it.name == name }
            ?.apply { isAccessible = true }
            ?.call(this, *args)

    private suspend fun <Input, Output>  Endpoint<Input, Output>.suspended(): Result<Output> = this.callPrivateFunc("justdoit")!! as Result<Output>
}
