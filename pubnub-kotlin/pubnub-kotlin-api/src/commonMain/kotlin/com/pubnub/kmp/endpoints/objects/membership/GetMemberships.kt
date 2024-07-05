package com.pubnub.kmp.endpoints.objects.membership

import com.pubnub.kmp.PNFuture
import com.pubnub.api.models.consumer.objects.membership.PNChannelMembershipArrayResult

/**
 * @see [PubNub.getMemberships]
 */
fun interface GetMemberships : PNFuture<PNChannelMembershipArrayResult>