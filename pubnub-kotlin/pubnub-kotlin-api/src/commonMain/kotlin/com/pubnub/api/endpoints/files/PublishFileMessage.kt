package com.pubnub.api.endpoints.files

import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.files.PNPublishFileMessageResult

/**
 * @see [PubNub.publishFileMessage]
 */
expect interface PublishFileMessage : Endpoint<PNPublishFileMessageResult>