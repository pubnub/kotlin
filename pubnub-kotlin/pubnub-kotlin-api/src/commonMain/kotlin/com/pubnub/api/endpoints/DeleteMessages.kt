package com.pubnub.api.endpoints

import com.pubnub.api.models.consumer.history.PNDeleteMessagesResult
import com.pubnub.kmp.PNFuture

/**
 * @see [com.pubnub.api.PubNub.deleteMessages]
 */
expect interface DeleteMessages : PNFuture<PNDeleteMessagesResult>
