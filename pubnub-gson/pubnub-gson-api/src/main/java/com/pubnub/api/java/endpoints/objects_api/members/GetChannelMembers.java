package com.pubnub.api.java.endpoints.objects_api.members;

import com.pubnub.api.java.endpoints.BuilderSteps;
import com.pubnub.api.java.endpoints.Endpoint;
import com.pubnub.api.java.endpoints.objects_api.utils.Include;
import com.pubnub.api.java.endpoints.objects_api.utils.PNSortKey;
import com.pubnub.api.java.models.consumer.objects_api.member.PNGetChannelMembersResult;

public interface GetChannelMembers extends Endpoint<PNGetChannelMembersResult> {
    GetChannelMembers limit(Integer limit);

    GetChannelMembers page(com.pubnub.api.models.consumer.objects.PNPage page);

    GetChannelMembers filter(String filter);

    GetChannelMembers sort(java.util.Collection<PNSortKey> sort);

    GetChannelMembers includeTotalCount(boolean includeTotalCount);

    GetChannelMembers includeCustom(boolean includeCustom);

    GetChannelMembers includeUUID(Include.PNUUIDDetailsLevel includeUUID);

    interface Builder extends BuilderSteps.ChannelStep<GetChannelMembers> {
        @Override
        GetChannelMembers channel(String channel);
    }
}
