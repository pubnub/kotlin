package com.pubnub.api.java.endpoints.objects_api.memberships;

import com.pubnub.api.java.endpoints.Endpoint;
import com.pubnub.api.java.endpoints.objects_api.utils.PNSortKey;
import com.pubnub.api.java.models.consumer.objects_api.membership.MembershipInclude;
import com.pubnub.api.java.models.consumer.objects_api.membership.PNGetMembershipsResult;

import java.util.Collection;

public interface GetMembershipsBuilder  extends Endpoint<PNGetMembershipsResult> {
    GetMembershipsBuilder limit(Integer limit);
    GetMembershipsBuilder page(com.pubnub.api.models.consumer.objects.PNPage page);
    GetMembershipsBuilder filter(String filter);
    GetMembershipsBuilder sort(Collection<PNSortKey> sort);
    GetMembershipsBuilder include(MembershipInclude include);
}
