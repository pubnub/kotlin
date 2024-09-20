package com.pubnub.api.endpoints.objects.membership

import com.pubnub.api.models.consumer.objects.membership.PNChannelMembershipArrayResult
import com.pubnub.kmp.PNFuture

/**
 * @see [PubNub.manageMemberships]
 */
actual interface ManageMemberships : PNFuture<PNChannelMembershipArrayResult>
