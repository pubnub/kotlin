package com.pubnub.api.models.consumer.push.payload

interface PushPayloadSerializer {

    fun toMap(): Map<String, Any>
}
