package com.pubnub.api.java.endpoints.objects_api.members;

import com.pubnub.api.java.endpoints.BuilderSteps;
import com.pubnub.api.java.endpoints.Endpoint;
import com.pubnub.api.java.endpoints.objects_api.utils.Include;
import com.pubnub.api.java.endpoints.objects_api.utils.ObjectsBuilderSteps;
import com.pubnub.api.java.endpoints.objects_api.utils.PNSortKey;
import com.pubnub.api.java.models.consumer.objects_api.member.PNSetChannelMembersResult;

import java.util.Collection;

@Deprecated
public interface SetChannelMembers extends Endpoint<PNSetChannelMembersResult> {

    SetChannelMembers limit(Integer limit);

    SetChannelMembers page(com.pubnub.api.models.consumer.objects.PNPage page);

    SetChannelMembers filter(String filter);

    SetChannelMembers sort(Collection<PNSortKey> sort);

    SetChannelMembers includeTotalCount(boolean includeTotalCount);

    SetChannelMembers includeCustom(boolean includeCustom);

    SetChannelMembers includeUUID(Include.PNUUIDDetailsLevel includeUUID);

    @Deprecated
    interface Builder extends BuilderSteps.ChannelStep<ObjectsBuilderSteps.UUIDsStep<SetChannelMembers>> {
        @Override
        ObjectsBuilderSteps.UUIDsStep<SetChannelMembers> channel(String channel);
    }
}
