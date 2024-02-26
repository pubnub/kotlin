package com.pubnub.api.endpoints.objects_api.memberships;

import com.pubnub.api.endpoints.Endpoint;
import com.pubnub.api.models.consumer.objects_api.membership.PNGetMembershipsResult;

public interface GetMemberships extends Endpoint<PNGetMembershipsResult> {
    GetMemberships uuid(String uuid);

    GetMemberships limit(Integer limit);

    GetMemberships page(com.pubnub.api.models.consumer.objects.PNPage page);

    GetMemberships filter(String filter);

    GetMemberships sort(java.util.Collection<com.pubnub.api.endpoints.objects_api.utils.PNSortKey> sort);

    GetMemberships includeTotalCount(boolean includeTotalCount);

    GetMemberships includeCustom(boolean includeCustom);

    GetMemberships includeChannel(com.pubnub.api.endpoints.objects_api.utils.Include.PNChannelDetailsLevel includeChannel);
}
