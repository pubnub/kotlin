package com.pubnub.api.java.endpoints.objects_api.members;

import com.pubnub.api.java.endpoints.Endpoint;
import com.pubnub.api.java.endpoints.objects_api.utils.PNSortKey;
import com.pubnub.api.java.models.consumer.objects_api.member.MemberInclude;
import com.pubnub.api.java.models.consumer.objects_api.member.PNManageChannelMembersResult;

import java.util.Collection;

public interface ManageChannelMembersBuilder extends Endpoint<PNManageChannelMembersResult> {
    ManageChannelMembersBuilder limit(Integer limit);
    ManageChannelMembersBuilder page(com.pubnub.api.models.consumer.objects.PNPage page);
    ManageChannelMembersBuilder filter(String filter);
    ManageChannelMembersBuilder sort(Collection<PNSortKey> sort);
    ManageChannelMembersBuilder include(MemberInclude include);
}
