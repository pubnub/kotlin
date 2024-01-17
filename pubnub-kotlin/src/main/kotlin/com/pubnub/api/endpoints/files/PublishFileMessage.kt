package com.pubnub.api.endpoints.files

import com.pubnub.api.Endpoint
import com.pubnub.api.PubNub
import com.pubnub.api.endpoints.remoteaction.mapIdentity
import com.pubnub.api.models.consumer.files.PNPublishFileMessageResult
import com.pubnub.internal.DelegatingEndpoint
import com.pubnub.internal.endpoints.files.IPublishFileMessage
import com.pubnub.internal.endpoints.files.PublishFileMessage

/**
 * @see [PubNub.publishFileMessage]
 */
class PublishFileMessage internal constructor(private val publishFileMessage: PublishFileMessage) :
    DelegatingEndpoint<PNPublishFileMessageResult>(), IPublishFileMessage by publishFileMessage {
    override fun createAction(): Endpoint<PNPublishFileMessageResult> = publishFileMessage.mapIdentity()
}