package com.pubnub.api.endpoints.files

import com.pubnub.api.Endpoint
import com.pubnub.api.PubNub
import com.pubnub.api.models.consumer.files.PNPublishFileMessageResult
import com.pubnub.internal.endpoints.files.IPublishFileMessage

/**
 * @see [PubNub.publishFileMessage]
 */
class PublishFileMessage internal constructor(publishFileMessage: IPublishFileMessage) :
    Endpoint<PNPublishFileMessageResult>(), IPublishFileMessage by publishFileMessage
