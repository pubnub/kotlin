package com.pubnub.kmp.endpoints

import com.pubnub.api.models.consumer.history.PNFetchMessagesResult
import com.pubnub.kmp.PNFuture

/**
 * @see [PubNub.fetchMessages]
 */
interface FetchMessages : PNFuture<PNFetchMessagesResult>