package com.pubnub.internal.endpoints

import com.pubnub.api.endpoints.DeleteMessages
import com.pubnub.api.models.consumer.history.PNDeleteMessagesResult
import com.pubnub.internal.EndpointImpl
import com.pubnub.internal.PubNubImpl

/**
 * @see [PubNubImpl.deleteMessages]
 */
class DeleteMessagesImpl internal constructor(deleteMessages: DeleteMessagesInterface) :
    DeleteMessagesInterface by deleteMessages,
    DeleteMessages,
    EndpointImpl<PNDeleteMessagesResult>(deleteMessages.configuration)