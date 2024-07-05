package com.pubnub.kmp.endpoints

import com.pubnub.kmp.PNFuture
import com.pubnub.api.models.consumer.history.PNDeleteMessagesResult

/**
 * @see [com.pubnub.api.PubNub.deleteMessages]
 */
interface DeleteMessages : PNFuture<PNDeleteMessagesResult>