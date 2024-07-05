package com.pubnub.api.endpoints.objects.membership

import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.objects.membership.PNChannelMembershipArrayResult

/**
 * @see [PubNub.manageMemberships]
 */
interface ManageMemberships : com.pubnub.kmp.endpoints.objects.membership.ManageMemberships, Endpoint<PNChannelMembershipArrayResult>
