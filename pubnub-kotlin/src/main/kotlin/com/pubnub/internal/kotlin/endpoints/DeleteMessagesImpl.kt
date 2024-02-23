package com.pubnub.internal.kotlin.endpoints

import com.pubnub.api.endpoints.DeleteMessages
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.endpoints.IDeleteMessages

/**
 * @see [PubNubImpl.deleteMessages]
 */
class DeleteMessagesImpl internal constructor(deleteMessages: IDeleteMessages) : IDeleteMessages by deleteMessages,
    DeleteMessages
