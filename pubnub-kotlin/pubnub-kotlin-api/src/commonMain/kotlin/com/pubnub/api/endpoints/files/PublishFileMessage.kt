package com.pubnub.api.endpoints.files

import com.pubnub.kmp.PNFuture
import com.pubnub.api.models.consumer.files.PNPublishFileMessageResult

/**
 * @see [PubNub.publishFileMessage]
 */
expect interface PublishFileMessage : PNFuture<PNPublishFileMessageResult>