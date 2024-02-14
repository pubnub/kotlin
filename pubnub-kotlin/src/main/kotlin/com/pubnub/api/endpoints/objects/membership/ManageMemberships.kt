package com.pubnub.api.endpoints.objects.membership

import com.pubnub.api.DelegatingEndpoint
import com.pubnub.api.PubNub
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction
import com.pubnub.api.models.consumer.objects.membership.PNChannelMembershipArrayResult
import com.pubnub.internal.endpoints.objects.membership.IManageMemberships
import com.pubnub.internal.endpoints.objects.membership.ManageMemberships

/**
 * @see [PubNub.manageMemberships]
 */
class ManageMemberships internal constructor(manageMemberships: ManageMemberships) :
    DelegatingEndpoint<PNChannelMembershipArrayResult, com.pubnub.internal.models.consumer.objects.membership.PNChannelMembershipArrayResult>(
        manageMemberships
    ),
    IManageMemberships by manageMemberships {
    override fun convertAction(remoteAction: ExtendedRemoteAction<com.pubnub.internal.models.consumer.objects.membership.PNChannelMembershipArrayResult>): ExtendedRemoteAction<PNChannelMembershipArrayResult> {
        return MappingRemoteAction(remoteAction, PNChannelMembershipArrayResult.Companion::from)
    }
}
