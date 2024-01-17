package com.pubnub.api.endpoints.objects.membership

import com.pubnub.api.Endpoint
import com.pubnub.api.PubNub
import com.pubnub.api.endpoints.remoteaction.map
import com.pubnub.api.models.consumer.objects.membership.PNChannelMembershipArrayResult
import com.pubnub.internal.DelegatingEndpoint
import com.pubnub.internal.endpoints.objects.membership.GetMemberships
import com.pubnub.internal.endpoints.objects.membership.IGetMemberships

/**
 * @see [PubNub.getMemberships]
 */
class GetMemberships internal constructor(private val getMemberships: GetMemberships) :
    DelegatingEndpoint<PNChannelMembershipArrayResult>(), IGetMemberships by getMemberships {

    override fun createAction(): Endpoint<PNChannelMembershipArrayResult> = getMemberships.map(PNChannelMembershipArrayResult::from)
}
