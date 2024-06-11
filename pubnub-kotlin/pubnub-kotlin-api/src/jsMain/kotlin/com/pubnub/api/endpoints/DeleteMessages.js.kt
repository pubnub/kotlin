package com.pubnub.api.endpoints

import com.pubnub.kmp.PNFuture
import com.pubnub.api.models.consumer.history.PNDeleteMessagesResult

/**
 * @see [com.pubnub.api.PubNub.deleteMessages]
 */
actual interface DeleteMessages : PNFuture<PNDeleteMessagesResult>