package com.pubnub.kmp.endpoints.files

import com.pubnub.kmp.PNFuture
import com.pubnub.api.models.consumer.files.PNPublishFileMessageResult

/**
 * @see [PubNub.publishFileMessage]
 */
interface PublishFileMessage : PNFuture<PNPublishFileMessageResult>