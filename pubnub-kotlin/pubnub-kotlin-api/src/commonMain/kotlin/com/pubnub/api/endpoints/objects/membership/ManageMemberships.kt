package com.pubnub.api.endpoints.objects.membership

import com.pubnub.kmp.PNFuture
import com.pubnub.api.models.consumer.objects.membership.PNChannelMembershipArrayResult

/**
 * @see [PubNub.manageMemberships]
 */
expect interface ManageMemberships : PNFuture<PNChannelMembershipArrayResult>