package com.pubnub.api.java.endpoints.objects_api.members;

import com.pubnub.api.java.endpoints.Endpoint;
import com.pubnub.api.java.endpoints.objects_api.utils.PNSortKey;
import com.pubnub.api.java.models.consumer.objects_api.member.MemberInclude;
import com.pubnub.api.java.models.consumer.objects_api.member.PNGetChannelMembersResult;

public interface GetChannelMembersBuilder extends Endpoint<PNGetChannelMembersResult>  {
    GetChannelMembersBuilder limit(Integer limit);
    GetChannelMembersBuilder page(com.pubnub.api.models.consumer.objects.PNPage page);
    GetChannelMembersBuilder filter(String filter);
    GetChannelMembersBuilder sort(java.util.Collection<PNSortKey> sort);
    GetChannelMembersBuilder include(MemberInclude include);
}
