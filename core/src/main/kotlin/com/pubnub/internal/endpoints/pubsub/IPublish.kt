package com.pubnub.internal.endpoints.pubsub

interface IPublish {
    val message: Any
    val channel: String
    val meta: Any?
    val shouldStore: Boolean?
    val usePost: Boolean
    val replicate: Boolean
    val ttl: Int?
}
