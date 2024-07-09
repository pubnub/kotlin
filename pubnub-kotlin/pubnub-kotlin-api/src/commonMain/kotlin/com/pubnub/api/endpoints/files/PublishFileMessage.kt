package com.pubnub.api.endpoints.files

import com.pubnub.api.models.consumer.files.PNPublishFileMessageResult
import com.pubnub.kmp.PNFuture

/**
 * @see [PubNub.publishFileMessage]
 */
expect interface PublishFileMessage : PNFuture<PNPublishFileMessageResult>
