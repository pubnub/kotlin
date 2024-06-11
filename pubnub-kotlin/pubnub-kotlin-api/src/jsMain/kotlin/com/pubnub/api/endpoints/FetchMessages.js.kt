package com.pubnub.api.endpoints

import PubNub
import com.pubnub.kmp.PNFuture
import com.pubnub.api.models.consumer.history.PNFetchMessagesResult

/**
 * @see [PubNub.fetchMessages]
 */
actual interface FetchMessages : PNFuture<PNFetchMessagesResult>

