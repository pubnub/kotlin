package com.pubnub.api.endpoints.files

import com.pubnub.api.Endpoint
import com.pubnub.internal.PubNubImpl
import com.pubnub.api.models.consumer.files.PNPublishFileMessageResult
import com.pubnub.internal.endpoints.files.IPublishFileMessage

/**
 * @see [PubNubImpl.publishFileMessage]
 */
class PublishFileMessage internal constructor(publishFileMessage: IPublishFileMessage) :
    Endpoint<PNPublishFileMessageResult>(), IPublishFileMessage by publishFileMessage
