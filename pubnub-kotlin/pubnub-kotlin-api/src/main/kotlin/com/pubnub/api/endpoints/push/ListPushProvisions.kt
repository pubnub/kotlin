package com.pubnub.api.endpoints.push

import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.push.PNPushListProvisionsResult

/**
 * @see [PubNub.auditPushChannelProvisions]
 */
interface ListPushProvisions : Endpoint<PNPushListProvisionsResult>
