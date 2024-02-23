package com.pubnub.internal.kotlin.endpoints.files

import com.pubnub.api.endpoints.files.PublishFileMessage
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.endpoints.files.IPublishFileMessage

/**
 * @see [PubNubImpl.publishFileMessage]
 */
class PublishFileMessageImpl internal constructor(publishFileMessage: IPublishFileMessage) :
    IPublishFileMessage by publishFileMessage,
        PublishFileMessage
