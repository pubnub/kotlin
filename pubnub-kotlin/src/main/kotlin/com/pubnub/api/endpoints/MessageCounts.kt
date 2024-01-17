package com.pubnub.api.endpoints

import com.pubnub.api.Endpoint
import com.pubnub.api.PubNub
import com.pubnub.api.endpoints.remoteaction.mapIdentity
import com.pubnub.api.models.consumer.history.PNMessageCountResult
import com.pubnub.internal.DelegatingEndpoint
import com.pubnub.internal.endpoints.IMessageCounts
import com.pubnub.internal.endpoints.MessageCounts

/**
 * @see [PubNub.messageCounts]
 */
class MessageCounts internal constructor(private val messageCounts: MessageCounts) : DelegatingEndpoint<PNMessageCountResult>(), IMessageCounts by messageCounts {
    override fun createAction(): Endpoint<PNMessageCountResult> = messageCounts.mapIdentity()
}
