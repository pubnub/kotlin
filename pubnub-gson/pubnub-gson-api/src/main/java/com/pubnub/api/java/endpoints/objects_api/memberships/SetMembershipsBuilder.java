package com.pubnub.api.java.endpoints.objects_api.memberships;

import com.pubnub.api.java.endpoints.Endpoint;
import com.pubnub.api.java.endpoints.objects_api.utils.PNSortKey;
import com.pubnub.api.java.models.consumer.objects_api.membership.PNSetMembershipResult;
import com.pubnub.api.models.consumer.objects.membership.MembershipInclude;

import java.util.Collection;

public interface SetMembershipsBuilder extends Endpoint<PNSetMembershipResult> {
    SetMembershipsBuilder userId(String userId);
    SetMembershipsBuilder limit(Integer limit);
    SetMembershipsBuilder page(com.pubnub.api.models.consumer.objects.PNPage page);
    SetMembershipsBuilder filter(String filter);
    SetMembershipsBuilder sort(Collection<PNSortKey> sort);
    SetMembershipsBuilder include(MembershipInclude include);
}
