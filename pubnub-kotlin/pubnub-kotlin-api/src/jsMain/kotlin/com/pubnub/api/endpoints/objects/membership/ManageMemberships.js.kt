package com.pubnub.api.endpoints.objects.membership

import PubNub
import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.objects.membership.PNChannelMembershipArrayResult

/**
 * @see [PubNub.manageMemberships]
 */
actual interface ManageMemberships : Endpoint<PNChannelMembershipArrayResult>