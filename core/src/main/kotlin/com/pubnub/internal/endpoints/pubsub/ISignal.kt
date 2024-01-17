package com.pubnub.internal.endpoints.pubsub

interface ISignal {
    val channel: String
    val message: Any
}
