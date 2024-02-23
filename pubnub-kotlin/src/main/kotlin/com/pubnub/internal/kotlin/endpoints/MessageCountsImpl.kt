package com.pubnub.internal.kotlin.endpoints

import com.pubnub.api.endpoints.MessageCounts
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.endpoints.IMessageCounts

/**
 * @see [PubNubImpl.messageCounts]
 */
class MessageCountsImpl internal constructor(messageCounts: IMessageCounts) : IMessageCounts by messageCounts,
    MessageCounts
