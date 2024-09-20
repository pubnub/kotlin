package com.pubnub.api.java.endpoints.objects_api.memberships;

import com.pubnub.api.java.endpoints.Endpoint;
import com.pubnub.api.java.endpoints.objects_api.utils.Include;
import com.pubnub.api.java.endpoints.objects_api.utils.ObjectsBuilderSteps;
import com.pubnub.api.java.endpoints.objects_api.utils.PNSortKey;
import com.pubnub.api.java.models.consumer.objects_api.membership.PNChannelMembership;
import com.pubnub.api.java.models.consumer.objects_api.membership.PNManageMembershipResult;

import java.util.Collection;

public interface ManageMemberships extends Endpoint<PNManageMembershipResult> {

    ManageMemberships set(Collection<PNChannelMembership> set);

    ManageMemberships remove(Collection<PNChannelMembership> remove);

    ManageMemberships uuid(String uuid);

    ManageMemberships limit(Integer limit);

    ManageMemberships page(com.pubnub.api.models.consumer.objects.PNPage page);

    ManageMemberships filter(String filter);

    ManageMemberships sort(Collection<PNSortKey> sort);

    ManageMemberships includeTotalCount(boolean includeTotalCount);

    ManageMemberships includeCustom(boolean includeCustom);

    ManageMemberships includeChannel(Include.PNChannelDetailsLevel includeChannel);

    interface Builder extends ObjectsBuilderSteps.RemoveOrSetStep<ManageMemberships, PNChannelMembership> {
        @Override
        RemoveStep<ManageMemberships, PNChannelMembership> set(Collection<PNChannelMembership> channelsToSet);

        @Override
        SetStep<ManageMemberships, PNChannelMembership> remove(Collection<PNChannelMembership> channelsToRemove);
    }

}
