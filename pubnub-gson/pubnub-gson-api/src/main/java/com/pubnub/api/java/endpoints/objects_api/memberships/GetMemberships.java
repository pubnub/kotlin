package com.pubnub.api.java.endpoints.objects_api.memberships;

import com.pubnub.api.java.endpoints.Endpoint;
import com.pubnub.api.java.endpoints.objects_api.utils.Include;
import com.pubnub.api.java.endpoints.objects_api.utils.PNSortKey;
import com.pubnub.api.java.models.consumer.objects_api.membership.PNGetMembershipsResult;

public interface GetMemberships extends Endpoint<PNGetMembershipsResult> {
    GetMemberships uuid(String uuid);

    GetMemberships limit(Integer limit);

    GetMemberships page(com.pubnub.api.models.consumer.objects.PNPage page);

    GetMemberships filter(String filter);

    GetMemberships sort(java.util.Collection<PNSortKey> sort);

    GetMemberships includeTotalCount(boolean includeTotalCount);

    GetMemberships includeCustom(boolean includeCustom);

    GetMemberships includeChannel(Include.PNChannelDetailsLevel includeChannel);
}
