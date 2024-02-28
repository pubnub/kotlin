package com.pubnub.api.endpoints.objects_api.members;

import com.pubnub.api.endpoints.BuilderSteps;
import com.pubnub.api.endpoints.Endpoint;
import com.pubnub.api.endpoints.objects_api.utils.ObjectsBuilderSteps;
import com.pubnub.api.models.consumer.objects_api.member.PNRemoveChannelMembersResult;

import java.util.Collection;

public interface RemoveChannelMembers extends Endpoint<PNRemoveChannelMembersResult> {

    RemoveChannelMembers limit(Integer limit);

    RemoveChannelMembers page(com.pubnub.api.models.consumer.objects.PNPage page);

    RemoveChannelMembers filter(String filter);

    RemoveChannelMembers sort(Collection<com.pubnub.api.endpoints.objects_api.utils.PNSortKey> sort);

    RemoveChannelMembers includeTotalCount(boolean includeTotalCount);

    RemoveChannelMembers includeCustom(boolean includeCustom);

    RemoveChannelMembers includeUUID(com.pubnub.api.endpoints.objects_api.utils.Include.PNUUIDDetailsLevel includeUUID);

    interface Builder extends BuilderSteps.ChannelStep<ObjectsBuilderSteps.UUIDsStep<RemoveChannelMembers>> {
        @Override
        ObjectsBuilderSteps.UUIDsStep<RemoveChannelMembers> channel(String channel);
    }
}
