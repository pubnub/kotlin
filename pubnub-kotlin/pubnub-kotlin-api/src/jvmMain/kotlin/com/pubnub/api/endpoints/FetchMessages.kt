package com.pubnub.api.endpoints

import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.PNBoundedPage
import com.pubnub.api.models.consumer.history.PNFetchMessagesResult

/**
 * @see [PubNub.fetchMessages]
 */
interface FetchMessages : com.pubnub.kmp.endpoints.FetchMessages, Endpoint<PNFetchMessagesResult> {
    val channels: List<String>
    val page: PNBoundedPage
    val includeUUID: Boolean
    val includeMeta: Boolean
    val includeMessageActions: Boolean
    val includeMessageType: Boolean
}
