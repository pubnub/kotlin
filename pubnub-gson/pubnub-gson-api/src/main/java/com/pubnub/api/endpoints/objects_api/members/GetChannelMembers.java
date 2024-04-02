package com.pubnub.api.endpoints.objects_api.members;

import com.pubnub.api.endpoints.BuilderSteps;
import com.pubnub.api.endpoints.Endpoint;
import com.pubnub.api.models.consumer.objects_api.member.PNGetChannelMembersResult;

public interface GetChannelMembers extends Endpoint<PNGetChannelMembersResult> {
    GetChannelMembers limit(Integer limit);

    GetChannelMembers page(com.pubnub.api.models.consumer.objects.PNPage page);

    GetChannelMembers filter(String filter);

    GetChannelMembers sort(java.util.Collection<com.pubnub.api.endpoints.objects_api.utils.PNSortKey> sort);

    GetChannelMembers includeTotalCount(boolean includeTotalCount);

    GetChannelMembers includeCustom(boolean includeCustom);

    GetChannelMembers includeUUID(com.pubnub.api.endpoints.objects_api.utils.Include.PNUUIDDetailsLevel includeUUID);

    interface Builder extends BuilderSteps.ChannelStep<GetChannelMembers> {
        @Override
        GetChannelMembers channel(String channel);
    }
}
