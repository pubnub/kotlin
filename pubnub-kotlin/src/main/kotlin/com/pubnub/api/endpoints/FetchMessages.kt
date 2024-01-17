package com.pubnub.api.endpoints

import com.pubnub.api.Endpoint
import com.pubnub.api.PubNub
import com.pubnub.api.endpoints.remoteaction.mapIdentity
import com.pubnub.api.models.consumer.history.PNFetchMessagesResult
import com.pubnub.internal.DelegatingEndpoint
import com.pubnub.internal.endpoints.FetchMessages
import com.pubnub.internal.endpoints.IFetchMessages

/**
 * @see [PubNub.fetchMessages]
 */
class FetchMessages internal constructor(private val fetchMessages: FetchMessages) : DelegatingEndpoint<PNFetchMessagesResult>(), IFetchMessages by fetchMessages {
    override fun createAction(): Endpoint<PNFetchMessagesResult> = fetchMessages.mapIdentity()
}
