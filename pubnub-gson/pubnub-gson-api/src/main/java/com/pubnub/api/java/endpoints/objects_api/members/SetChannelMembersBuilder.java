package com.pubnub.api.java.endpoints.objects_api.members;

import com.pubnub.api.java.endpoints.Endpoint;
import com.pubnub.api.java.endpoints.objects_api.utils.PNSortKey;
import com.pubnub.api.java.models.consumer.objects_api.member.MemberInclude;
import com.pubnub.api.java.models.consumer.objects_api.member.PNSetChannelMembersResult;

import java.util.Collection;

public interface SetChannelMembersBuilder extends Endpoint<PNSetChannelMembersResult> {
    SetChannelMembers limit(Integer limit);
    SetChannelMembers page(com.pubnub.api.models.consumer.objects.PNPage page);
    SetChannelMembers filter(String filter);
    SetChannelMembers sort(Collection<PNSortKey> sort);
    SetChannelMembers include(MemberInclude include);
}
