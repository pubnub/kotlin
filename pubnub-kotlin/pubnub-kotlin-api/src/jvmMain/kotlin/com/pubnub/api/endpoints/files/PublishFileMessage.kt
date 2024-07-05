package com.pubnub.api.endpoints.files

import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.files.PNPublishFileMessageResult

/**
 * @see [PubNub.publishFileMessage]
 */
interface PublishFileMessage : com.pubnub.kmp.endpoints.files.PublishFileMessage, Endpoint<PNPublishFileMessageResult>
