package com.pubnub.internal.models.consumer.push.payload

interface PushPayloadSerializer {
    fun toMap(): Map<String, Any>
}
