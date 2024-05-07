package com.pubnub.api.models.consumer.pubsub

interface PNEvent {
    val channel: String
    val subscription: String?
    val timetoken: Long?
}
