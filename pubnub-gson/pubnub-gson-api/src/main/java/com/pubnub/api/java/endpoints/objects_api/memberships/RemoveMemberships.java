package com.pubnub.api.java.endpoints.objects_api.memberships;

import com.pubnub.api.java.endpoints.Endpoint;
import com.pubnub.api.java.endpoints.objects_api.utils.Include;
import com.pubnub.api.java.endpoints.objects_api.utils.ObjectsBuilderSteps;
import com.pubnub.api.java.endpoints.objects_api.utils.PNSortKey;
import com.pubnub.api.java.models.consumer.objects_api.membership.PNChannelMembership;
import com.pubnub.api.java.models.consumer.objects_api.membership.PNRemoveMembershipResult;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public interface RemoveMemberships extends Endpoint<PNRemoveMembershipResult> {

    RemoveMemberships uuid(String uuid);

    RemoveMemberships limit(Integer limit);

    RemoveMemberships page(com.pubnub.api.models.consumer.objects.PNPage page);

    RemoveMemberships filter(String filter);

    RemoveMemberships sort(Collection<PNSortKey> sort);

    RemoveMemberships includeTotalCount(boolean includeTotalCount);

    RemoveMemberships includeCustom(boolean includeCustom);

    RemoveMemberships includeChannel(Include.PNChannelDetailsLevel includeChannel);

    interface Builder extends ObjectsBuilderSteps.ChannelMembershipsStep<RemoveMemberships> {
        @Override
        RemoveMemberships channelMemberships(@NotNull Collection<PNChannelMembership> channelMemberships);
    }
}
