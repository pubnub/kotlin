package com.pubnub.api.endpoints

import com.pubnub.api.models.consumer.history.PNFetchMessagesResult
import com.pubnub.kmp.PNFuture

/**
 * @see [PubNub.fetchMessages]
 */
actual interface FetchMessages : PNFuture<PNFetchMessagesResult>
