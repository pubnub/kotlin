package com.pubnub.internal.endpoints

import com.pubnub.api.endpoints.DeleteMessages
import com.pubnub.internal.PubNubImpl

/**
 * @see [PubNubImpl.deleteMessages]
 */
class DeleteMessagesImpl internal constructor(deleteMessages: DeleteMessagesInterface) :
    DeleteMessagesInterface by deleteMessages,
    DeleteMessages
