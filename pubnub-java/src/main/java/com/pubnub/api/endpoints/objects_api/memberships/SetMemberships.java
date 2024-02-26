package com.pubnub.api.endpoints.objects_api.memberships;

import com.pubnub.api.endpoints.Endpoint;
import com.pubnub.api.models.consumer.objects_api.membership.PNChannelMembership;
import com.pubnub.api.models.consumer.objects_api.membership.PNSetMembershipResult;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public interface SetMemberships extends Endpoint<PNSetMembershipResult> {
    SetMemberships uuid(String uuid);

    SetMemberships limit(Integer limit);

    SetMemberships page(com.pubnub.api.models.consumer.objects.PNPage page);

    SetMemberships filter(String filter);

    SetMemberships sort(java.util.Collection<com.pubnub.api.endpoints.objects_api.utils.PNSortKey> sort);

    SetMemberships includeTotalCount(boolean includeTotalCount);

    SetMemberships includeCustom(boolean includeCustom);

    SetMemberships includeChannel(com.pubnub.api.endpoints.objects_api.utils.Include.PNChannelDetailsLevel includeChannel);

    interface Builder {
        SetMemberships channelMemberships(@NotNull Collection<PNChannelMembership> channelMemberships);
    }
}
