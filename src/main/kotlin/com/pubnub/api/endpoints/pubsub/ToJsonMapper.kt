package com.pubnub.api.endpoints.pubsub

interface ToJsonMapper {
    fun toJson(input: Any?): String
}
