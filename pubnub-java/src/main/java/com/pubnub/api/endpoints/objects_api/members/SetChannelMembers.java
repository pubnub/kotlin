package com.pubnub.api.endpoints.objects_api.members;

import com.pubnub.api.endpoints.BuilderSteps;
import com.pubnub.api.endpoints.Endpoint;
import com.pubnub.api.endpoints.objects_api.utils.ObjectsBuilderSteps;
import com.pubnub.api.models.consumer.objects_api.member.PNSetChannelMembersResult;

import java.util.Collection;

public interface SetChannelMembers extends Endpoint<PNSetChannelMembersResult> {

    SetChannelMembers limit(Integer limit);

    SetChannelMembers page(com.pubnub.api.models.consumer.objects.PNPage page);

    SetChannelMembers filter(String filter);

    SetChannelMembers sort(Collection<com.pubnub.api.endpoints.objects_api.utils.PNSortKey> sort);

    SetChannelMembers includeTotalCount(boolean includeTotalCount);

    SetChannelMembers includeCustom(boolean includeCustom);

    SetChannelMembers includeUUID(com.pubnub.api.endpoints.objects_api.utils.Include.PNUUIDDetailsLevel includeUUID);

    interface Builder extends BuilderSteps.ChannelStep<ObjectsBuilderSteps.UUIDsStep<SetChannelMembers>> {
        @Override
        ObjectsBuilderSteps.UUIDsStep<SetChannelMembers> channel(String channel);
    }
}
