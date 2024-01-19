package com.pubnub.api.endpoints.pubsub

import com.pubnub.api.Endpoint
import com.pubnub.api.PubNub
import com.pubnub.api.endpoints.remoteaction.mapIdentity
import com.pubnub.api.models.consumer.PNPublishResult
import com.pubnub.internal.DelegatingEndpoint
import com.pubnub.internal.endpoints.pubsub.IPublish
import com.pubnub.internal.endpoints.pubsub.Publish

/**
 * @see [PubNub.publish]
 */
class Publish internal constructor(private val publish: Publish) : DelegatingEndpoint<PNPublishResult>(), IPublish by publish {
    override fun createAction(): Endpoint<PNPublishResult> = publish.mapIdentity()
}
