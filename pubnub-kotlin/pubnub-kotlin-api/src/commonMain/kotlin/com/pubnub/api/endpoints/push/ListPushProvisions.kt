package com.pubnub.api.endpoints.push

import com.pubnub.kmp.PNFuture
import com.pubnub.api.models.consumer.push.PNPushListProvisionsResult

/**
 * @see [PubNub.auditPushChannelProvisions]
 */
expect interface ListPushProvisions : PNFuture<PNPushListProvisionsResult>