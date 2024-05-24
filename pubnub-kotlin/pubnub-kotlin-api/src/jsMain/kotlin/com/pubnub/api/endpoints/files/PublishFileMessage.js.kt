package com.pubnub.api.endpoints.files

import PubNub
import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.files.PNPublishFileMessageResult

/**
 * @see [PubNub.publishFileMessage]
 */
actual interface PublishFileMessage : Endpoint<PNPublishFileMessageResult>