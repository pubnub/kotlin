package com.pubnub.api.endpoints.objects.membership

import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.objects.membership.PNChannelMembershipArrayResult

/**
 * @see [PubNub.getMemberships]
 */
interface GetMemberships : com.pubnub.kmp.endpoints.objects.membership.GetMemberships, Endpoint<PNChannelMembershipArrayResult>
