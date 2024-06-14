package com.pubnub.api.endpoints.push

import PubNub
import com.pubnub.kmp.PNFuture
import com.pubnub.api.models.consumer.push.PNPushListProvisionsResult

/**
 * @see [PubNub.auditPushChannelProvisions]
 */
actual interface ListPushProvisions : PNFuture<PNPushListProvisionsResult>