package com.pubnub.api.endpoints

import com.pubnub.api.Endpoint
import com.pubnub.api.PubNub
import com.pubnub.api.endpoints.remoteaction.mapIdentity
import com.pubnub.api.models.consumer.history.PNDeleteMessagesResult
import com.pubnub.internal.DelegatingEndpoint
import com.pubnub.internal.endpoints.DeleteMessages
import com.pubnub.internal.endpoints.IDeleteMessages

/**
 * @see [PubNub.deleteMessages]
 */
class DeleteMessages internal constructor(private val deleteMessages: DeleteMessages) : DelegatingEndpoint<PNDeleteMessagesResult>(), IDeleteMessages by deleteMessages {
    override fun createAction(): Endpoint<PNDeleteMessagesResult> = deleteMessages.mapIdentity()
}
