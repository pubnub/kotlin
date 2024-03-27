package com.pubnub.internal.endpoints

import com.pubnub.api.endpoints.MessageCounts
import com.pubnub.internal.PubNubImpl

/**
 * @see [PubNubImpl.messageCounts]
 */
class MessageCountsImpl internal constructor(messageCounts: MessageCountsInterface) :
    MessageCountsInterface by messageCounts,
    MessageCounts
