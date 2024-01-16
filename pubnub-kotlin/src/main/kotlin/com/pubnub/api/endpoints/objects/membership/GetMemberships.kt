package com.pubnub.api.endpoints.objects.membership

import com.pubnub.api.DelegatingEndpoint
import com.pubnub.api.PubNub
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction
import com.pubnub.api.models.consumer.objects.membership.PNChannelMembershipArrayResult
import com.pubnub.internal.endpoints.objects.membership.GetMemberships
import com.pubnub.internal.endpoints.objects.membership.IGetMemberships

/**
 * @see [PubNub.getMemberships]
 */
class GetMemberships internal constructor(getMemberships: GetMemberships) :
    DelegatingEndpoint<PNChannelMembershipArrayResult, com.pubnub.internal.models.consumer.objects.membership.PNChannelMembershipArrayResult>(
        getMemberships
    ), IGetMemberships by getMemberships {
    override fun convertAction(remoteAction: ExtendedRemoteAction<com.pubnub.internal.models.consumer.objects.membership.PNChannelMembershipArrayResult>): ExtendedRemoteAction<PNChannelMembershipArrayResult> {
        return MappingRemoteAction(remoteAction, PNChannelMembershipArrayResult::from)
    }
}