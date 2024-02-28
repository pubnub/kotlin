package com.pubnub.internal.endpoints.objects.membership

import com.pubnub.api.DelegatingEndpoint
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction
import com.pubnub.api.models.consumer.objects.membership.PNChannelMembershipArrayResult
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.models.toApi

/**
 * @see [PubNubImpl.getMemberships]
 */
class GetMembershipsImpl internal constructor(getMemberships: GetMembershipsEndpoint) :
    DelegatingEndpoint<PNChannelMembershipArrayResult, com.pubnub.internal.models.consumer.objects.membership.PNChannelMembershipArrayResult>(
        getMemberships,
    ),
    GetMembershipsInterface by getMemberships,
    com.pubnub.api.endpoints.objects.membership.GetMemberships {
        override fun convertAction(
            remoteAction: ExtendedRemoteAction<com.pubnub.internal.models.consumer.objects.membership.PNChannelMembershipArrayResult>,
        ): ExtendedRemoteAction<PNChannelMembershipArrayResult> {
            return MappingRemoteAction(
                remoteAction,
                com.pubnub.internal.models.consumer.objects.membership.PNChannelMembershipArrayResult::toApi,
            )
        }
    }
