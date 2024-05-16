package com.pubnub.api.endpoints

import PubNub
import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.history.PNFetchMessagesResult

/**
 * @see [PubNub.fetchMessages]
 */
actual interface FetchMessages : Endpoint<PNFetchMessagesResult>

