package com.pubnub.api.endpoints.pubsub

import com.pubnub.api.services.PublishService
import retrofit2.Call

class PublishServiceExternalImpl constructor(
    val publishService: PublishService
) : PublishServiceExternal {
    override fun publish(
        pubKey: String,
        subKey: String,
        channel: String,
        message: String,
        options: Map<String, String>
    ): Call<List<Any>> {
        return publishService.publish(
            pubKey = pubKey,
            subKey = subKey,
            channel = channel,
            message = message,
            options = options
        )
    }

    override fun publishWithPost(
        pubKey: String,
        subKey: String,
        channel: String,
        message: Any,
        options: Map<String, String>
    ): Call<List<Any>> {
        return publishService.publishWithPost(
            pubKey = pubKey,
            subKey = subKey,
            channel = channel,
            body = message,
            options = options
        )
    }
}
