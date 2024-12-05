package com.pubnub.api.java.endpoints.objects_api.memberships;

import com.pubnub.api.java.endpoints.Endpoint;
import com.pubnub.api.java.endpoints.objects_api.utils.PNSortKey;
import com.pubnub.api.java.models.consumer.objects_api.membership.MembershipInclude;
import com.pubnub.api.java.models.consumer.objects_api.membership.PNManageMembershipResult;

import java.util.Collection;

public interface ManageMembershipsBuilder  extends Endpoint<PNManageMembershipResult> {
    ManageMembershipsBuilder userId(String userId);
    ManageMembershipsBuilder limit(Integer limit);
    ManageMembershipsBuilder page(com.pubnub.api.models.consumer.objects.PNPage page);
    ManageMembershipsBuilder filter(String filter);
    ManageMembershipsBuilder sort(Collection<PNSortKey> sort);
    ManageMembershipsBuilder include(MembershipInclude include);
}
