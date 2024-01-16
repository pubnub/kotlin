package com.pubnub.api.endpoints

import com.pubnub.api.PubNub
import com.pubnub.internal.endpoints.IMessageCounts

/**
 * @see [PubNub.messageCounts]
 */
class MessageCounts internal constructor(messageCounts: IMessageCounts) : IMessageCounts by messageCounts
