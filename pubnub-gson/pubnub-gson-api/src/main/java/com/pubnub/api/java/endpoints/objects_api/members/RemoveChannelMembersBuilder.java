package com.pubnub.api.java.endpoints.objects_api.members;

import com.pubnub.api.java.endpoints.Endpoint;
import com.pubnub.api.java.endpoints.objects_api.utils.PNSortKey;
import com.pubnub.api.java.models.consumer.objects_api.member.MemberInclude;
import com.pubnub.api.java.models.consumer.objects_api.member.PNRemoveChannelMembersResult;

import java.util.Collection;

public interface RemoveChannelMembersBuilder extends Endpoint<PNRemoveChannelMembersResult> {
    RemoveChannelMembersBuilder limit(Integer limit);
    RemoveChannelMembersBuilder page(com.pubnub.api.models.consumer.objects.PNPage page);
    RemoveChannelMembersBuilder filter(String filter);
    RemoveChannelMembersBuilder sort(Collection<PNSortKey> sort);
    RemoveChannelMembersBuilder include(MemberInclude include);
}
