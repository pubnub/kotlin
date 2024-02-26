package com.pubnub.internal.endpoints

import com.pubnub.api.endpoints.DeleteMessages
import com.pubnub.internal.PubNubImpl

/**
 * @see [PubNubImpl.deleteMessages]
 */
class DeleteMessagesImpl internal constructor(deleteMessages: IDeleteMessages) :
    IDeleteMessages by deleteMessages,
    DeleteMessages
