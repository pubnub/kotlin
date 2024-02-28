package com.pubnub.api.endpoints.objects_api.memberships;

import com.pubnub.api.endpoints.Endpoint;
import com.pubnub.api.endpoints.objects_api.utils.ObjectsBuilderSteps;
import com.pubnub.api.models.consumer.objects_api.membership.PNChannelMembership;
import com.pubnub.api.models.consumer.objects_api.membership.PNRemoveMembershipResult;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public interface RemoveMemberships extends Endpoint<PNRemoveMembershipResult> {

    RemoveMemberships uuid(String uuid);

    RemoveMemberships limit(Integer limit);

    RemoveMemberships page(com.pubnub.api.models.consumer.objects.PNPage page);

    RemoveMemberships filter(String filter);

    RemoveMemberships sort(java.util.Collection<com.pubnub.api.endpoints.objects_api.utils.PNSortKey> sort);

    RemoveMemberships includeTotalCount(boolean includeTotalCount);

    RemoveMemberships includeCustom(boolean includeCustom);

    RemoveMemberships includeChannel(com.pubnub.api.endpoints.objects_api.utils.Include.PNChannelDetailsLevel includeChannel);

    interface Builder extends ObjectsBuilderSteps.ChannelMembershipsStep<RemoveMemberships> {
        @Override
        RemoveMemberships channelMemberships(@NotNull Collection<PNChannelMembership> channelMemberships);
    }
}
