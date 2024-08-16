package com.pubnub.api.java.endpoints.objects_api.memberships;

import com.pubnub.api.java.endpoints.Endpoint;
import com.pubnub.api.java.endpoints.objects_api.utils.Include;
import com.pubnub.api.java.endpoints.objects_api.utils.PNSortKey;
import com.pubnub.api.java.models.consumer.objects_api.membership.PNChannelMembership;
import com.pubnub.api.java.models.consumer.objects_api.membership.PNSetMembershipResult;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public interface SetMemberships extends Endpoint<PNSetMembershipResult> {
    SetMemberships uuid(String uuid);

    SetMemberships limit(Integer limit);

    SetMemberships page(com.pubnub.api.models.consumer.objects.PNPage page);

    SetMemberships filter(String filter);

    SetMemberships sort(Collection<PNSortKey> sort);

    SetMemberships includeTotalCount(boolean includeTotalCount);

    SetMemberships includeCustom(boolean includeCustom);

    SetMemberships includeChannel(Include.PNChannelDetailsLevel includeChannel);

    interface Builder {
        SetMemberships channelMemberships(@NotNull Collection<PNChannelMembership> channelMemberships);
    }
}
