package com.pubnub.internal.endpoints.pubsub

import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction
import com.pubnub.api.models.consumer.PNPublishResult

interface IPublish : ExtendedRemoteAction<PNPublishResult> {
    val message: Any
    val channel: String
    val meta: Any?
    val shouldStore: Boolean?
    val usePost: Boolean
    val replicate: Boolean
    val ttl: Int?
}