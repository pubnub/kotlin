package com.pubnub.api.endpoints

import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.history.PNFetchMessagesResult

/**
 * @see [PubNub.fetchMessages]
 */
expect interface FetchMessages : Endpoint<PNFetchMessagesResult> {
}