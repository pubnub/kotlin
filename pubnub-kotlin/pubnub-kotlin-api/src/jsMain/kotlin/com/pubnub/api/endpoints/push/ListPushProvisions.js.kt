package com.pubnub.api.endpoints.push

import com.pubnub.api.models.consumer.push.PNPushListProvisionsResult
import com.pubnub.kmp.PNFuture

/**
 * @see [PubNub.auditPushChannelProvisions]
 */
actual interface ListPushProvisions : PNFuture<PNPushListProvisionsResult>
