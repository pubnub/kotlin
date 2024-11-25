package com.pubnub.api.java.endpoints.objects_api.memberships;

import com.pubnub.api.java.endpoints.Endpoint;
import com.pubnub.api.java.endpoints.objects_api.utils.PNSortKey;
import com.pubnub.api.java.models.consumer.objects_api.membership.MembershipInclude;
import com.pubnub.api.java.models.consumer.objects_api.membership.PNRemoveMembershipResult;

import java.util.Collection;

public interface RemoveMembershipsBuilder extends Endpoint<PNRemoveMembershipResult> {
    RemoveMembershipsBuilder userId(String userId);
    RemoveMembershipsBuilder limit(Integer limit);
    RemoveMembershipsBuilder page(com.pubnub.api.models.consumer.objects.PNPage page);
    RemoveMembershipsBuilder filter(String filter);
    RemoveMembershipsBuilder sort(Collection<PNSortKey> sort);
    RemoveMembershipsBuilder include(MembershipInclude include);
}
