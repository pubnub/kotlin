package com.pubnub.api

import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.v2.PNConfiguration
import com.pubnub.api.v2.callbacks.EventListener

actual fun createCommonPubNub(config: PNConfiguration): PubNub {
    return PubNub.create(config)
}

actual fun EventListener(onMessage: (PubNub, PNMessageResult) -> Unit): EventListener {
    return object : EventListener {
        override fun message(pubnub: PubNub, result: PNMessageResult) {
            onMessage(pubnub, result)
        }
    }
}