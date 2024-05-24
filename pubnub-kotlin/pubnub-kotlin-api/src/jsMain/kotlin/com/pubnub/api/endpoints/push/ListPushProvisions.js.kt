package com.pubnub.api.endpoints.push

import PubNub
import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.push.PNPushListProvisionsResult

/**
 * @see [PubNub.auditPushChannelProvisions]
 */
actual interface ListPushProvisions : Endpoint<PNPushListProvisionsResult>