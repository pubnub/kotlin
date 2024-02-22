package com.pubnub.api.endpoints

import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.endpoints.IMessageCounts

/**
 * @see [PubNubImpl.messageCounts]
 */
class MessageCounts internal constructor(messageCounts: IMessageCounts) : IMessageCounts by messageCounts
