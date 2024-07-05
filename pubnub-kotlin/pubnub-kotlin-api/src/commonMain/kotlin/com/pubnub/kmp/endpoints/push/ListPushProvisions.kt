package com.pubnub.kmp.endpoints.push

import com.pubnub.kmp.PNFuture
import com.pubnub.api.models.consumer.push.PNPushListProvisionsResult

/**
 * @see [PubNub.auditPushChannelProvisions]
 */
interface ListPushProvisions : PNFuture<PNPushListProvisionsResult>