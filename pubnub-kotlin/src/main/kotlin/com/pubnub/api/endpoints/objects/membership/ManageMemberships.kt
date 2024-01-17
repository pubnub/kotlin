package com.pubnub.api.endpoints.objects.membership

import com.pubnub.api.Endpoint
import com.pubnub.api.PubNub
import com.pubnub.api.endpoints.remoteaction.map
import com.pubnub.api.models.consumer.objects.membership.PNChannelMembershipArrayResult
import com.pubnub.internal.DelegatingEndpoint
import com.pubnub.internal.endpoints.objects.membership.IManageMemberships
import com.pubnub.internal.endpoints.objects.membership.ManageMemberships

/**
 * @see [PubNub.manageMemberships]
 */
class ManageMemberships internal constructor(private val manageMemberships: ManageMemberships) :
    DelegatingEndpoint<PNChannelMembershipArrayResult>(), IManageMemberships by manageMemberships {

    override fun createAction(): Endpoint<PNChannelMembershipArrayResult> = manageMemberships.map(PNChannelMembershipArrayResult::from)
}