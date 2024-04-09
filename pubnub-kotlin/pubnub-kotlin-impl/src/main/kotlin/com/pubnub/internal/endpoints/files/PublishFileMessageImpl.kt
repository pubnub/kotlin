package com.pubnub.internal.endpoints.files

import com.pubnub.api.endpoints.files.PublishFileMessage
import com.pubnub.api.models.consumer.files.PNPublishFileMessageResult
import com.pubnub.internal.EndpointImpl
import com.pubnub.internal.PubNubImpl

/**
 * @see [PubNubImpl.publishFileMessage]
 */
class PublishFileMessageImpl internal constructor(publishFileMessage: PublishFileMessageInterface) :
    PublishFileMessageInterface by publishFileMessage,
    PublishFileMessage,
    EndpointImpl<PNPublishFileMessageResult>(publishFileMessage)
